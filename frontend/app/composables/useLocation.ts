const GEOLOCATION_PERMISSION_COOKIE_KEY = 'user-geolocation-permission';
type LocationPermissionStatus = 'allowed' | 'denied' | null;
type LocationRequestResult = 'allowed' | 'blocked' | 'error';

const LOCATION_OPTIONS: PositionOptions = {
  enableHighAccuracy: true,
  timeout: 5000,
  maximumAge: 0,
};

const PERMISSION_MAX_AGE: Record<Exclude<LocationPermissionStatus, null>, number> = {
  allowed: 60 * 60 * 24 * 365,
  denied: 60 * 60 * 24 * 7,
};

export function useLocation() {
  const isInitialized = useState<boolean>('location-initialized', () => false);

  const permissionCookie = useCookie<LocationPermissionStatus>(GEOLOCATION_PERMISSION_COOKIE_KEY, {
    path: '/',
    sameSite: 'lax',
  });

  const permissionStatus = useState<LocationPermissionStatus>(
    'location-permission-status',
    () => permissionCookie.value ?? null,
  );

  const latitude = ref<number | null>(null);
  const longitude = ref<number | null>(null);

  const updatePermission = (status: LocationPermissionStatus) => {
    if (!import.meta.client) return;

    permissionStatus.value = status;

    const cookieWithAge = useCookie<LocationPermissionStatus>(GEOLOCATION_PERMISSION_COOKIE_KEY, {
      maxAge: status === null ? -1 : PERMISSION_MAX_AGE[status],
      path: '/',
      sameSite: 'lax',
    });
    cookieWithAge.value = status;
  };

  async function requestLocation(): Promise<LocationRequestResult> {
    if (!import.meta.client || !('geolocation' in navigator)) return 'error';

    return new Promise<LocationRequestResult>((resolve) => {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          latitude.value = position.coords.latitude;
          longitude.value = position.coords.longitude;
          console.log('Longitude:', longitude.value);
          console.log('Latitude:', latitude.value);
          updatePermission('allowed');
          resolve('allowed');
        },
        (err) => {
          if (err.code === err.PERMISSION_DENIED) {
            updatePermission('denied');
            resolve('blocked');
          } else {
            resolve('error');
          }
        },
        LOCATION_OPTIONS,
      );
    });
  }

  async function initializeLocation() {
    if (!import.meta.client || isInitialized.value) return;

    try {
      if (permissionCookie.value === 'denied') {
        permissionStatus.value = 'denied';
        return;
      }

      if (permissionCookie.value === 'allowed') {
        await requestLocation();
        return;
      }

      if (!('permissions' in navigator)) {
        permissionStatus.value = null;
        return;
      }

      const permission = await navigator.permissions.query({ name: 'geolocation' });

      if (permission.state === 'granted') {
        updatePermission('allowed');
        await requestLocation();
      } else if (permission.state === 'denied') {
        permissionStatus.value = 'denied';
      } else {
        permissionStatus.value = null;
      }
    } catch (error) {
      console.warn('Permissions API check skipped:', error);
      if (permissionCookie.value === null) {
        permissionStatus.value = null;
      }
    } finally {
      isInitialized.value = true;
    }
  }

  return {
    latitude,
    longitude,
    permissionStatus,
    isInitialized,
    requestLocation,
    initializeLocation,
    setPermissionDenied: () => updatePermission('denied'),
  };
}
