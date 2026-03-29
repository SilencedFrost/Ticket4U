const GEOLOCATION_PERMISSION_COOKIE_KEY = 'user-geolocation-permission';
type LocationPermissionStatus = 'allowed' | 'denied' | null;

export function useLocation() {
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

    const maxAge =
      status === 'allowed' ? 60 * 60 * 24 * 365 : status === 'denied' ? 60 * 60 * 24 * 7 : -1;

    const cookieInstance = useCookie(GEOLOCATION_PERMISSION_COOKIE_KEY, {
      maxAge,
      path: '/',
      sameSite: 'lax',
    });
    cookieInstance.value = status;
  };

  async function requestLocation() {
    if (!import.meta.client || !('geolocation' in navigator)) return 'error';

    return new Promise<'allowed' | 'blocked' | 'error'>((resolve) => {
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
        { enableHighAccuracy: true, timeout: 5000, maximumAge: 0 },
      );
    });
  }

  async function initializeLocation() {
    if (!import.meta.client) return;

    if (permissionCookie.value === 'denied') {
      permissionStatus.value = 'denied';
      return;
    }

    if (permissionCookie.value === 'allowed') {
      await requestLocation();
      return;
    }

    try {
      if (!('permissions' in navigator)) return;

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
    }
  }

  return {
    latitude,
    longitude,
    permissionStatus,
    requestLocation,
    initializeLocation,
    setPermissionDenied: () => updatePermission('denied'),
  };
}
