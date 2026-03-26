const GEOLOCATION_OPTIONS = {
  enableHighAccuracy: true,
  timeout: 5000,
  maximumAge: 0,
};

export function useLocation() {
  const latitude = ref<number | null>(null);
  const longitude = ref<number | null>(null);

  function isGeolocationSupported(): boolean {
    return typeof window !== 'undefined' && 'geolocation' in navigator;
  }

  function handleSuccess(position: GeolocationPosition): void {
    latitude.value = position.coords.latitude;
    longitude.value = position.coords.longitude;

    console.log('Longitude:', longitude.value);
    console.log('Latitude:', latitude.value);
  }

  function handleError(err: GeolocationPositionError): void {
    console.error('Error getting location:', err.message);
  }

  function requestLocation(): void {
    if (!isGeolocationSupported()) {
      console.warn('Geolocation is not supported by this browser.');
      return;
    }

    navigator.geolocation.getCurrentPosition(handleSuccess, handleError, GEOLOCATION_OPTIONS);
  }

  return {
    latitude,
    longitude,
    requestLocation,
    isGeolocationSupported,
  };
}
