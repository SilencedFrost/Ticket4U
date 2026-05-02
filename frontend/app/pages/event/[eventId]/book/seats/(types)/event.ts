// TODO: Resolve no content error
export interface Event {
  id: string;
  title: string;
  date: string;
  time: string;
  venue: string;
  bannerUrl?: {
    wide: string;
    square: string;
    tall: string;
  };
}
