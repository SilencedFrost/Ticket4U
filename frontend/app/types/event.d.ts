export {}

declare global {
  interface EventData { // 'cause "Event" is a built-in interface, if we name it Event, it will cause conflicts
    id: number;
    image: string;
    title: string;
    price: string;
    date: string;
  }
}