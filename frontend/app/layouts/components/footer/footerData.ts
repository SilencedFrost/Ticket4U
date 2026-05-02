export type FooterSectionId = 'events' | 'about' | 'partners' | 'contact';

type FooterLinkItem = {
  labelKey: string;
  href: string;
};

type FooterLinksSection = {
  id: Exclude<FooterSectionId, 'contact'>;
  type: 'links';
  titleKey: string;
  items: FooterLinkItem[];
};

type FooterContactSection = {
  id: 'contact';
  type: 'contact';
  titleKey: string;
};

export type FooterSection = FooterLinksSection | FooterContactSection;

export const FOOTER_CONTACT = {
  phoneLabelKey: 'footer.labels.phone',
  phoneText: '0912 345 678',
  emailLabelKey: 'footer.labels.email',
  email: 'supportagent@gmail.com',
} as const;

export const FOOTER_SECTIONS: FooterSection[] = [
  {
    id: 'events',
    type: 'links',
    titleKey: 'common.events',
    items: [
      { labelKey: 'footer.menu.all_events', href: '#' },
      { labelKey: 'footer.menu.popular_events', href: '#' },
      { labelKey: 'footer.menu.upcoming_events', href: '#' },
      { labelKey: 'footer.menu.nearby_events', href: '#' },
      { labelKey: 'footer.menu.free_events', href: '#' },
    ],
  },
  {
    id: 'about',
    type: 'links',
    titleKey: 'common.about_us',
    items: [
      { labelKey: 'footer.menu.our_story', href: '#' },
      { labelKey: 'footer.menu.team', href: '#' },
      { labelKey: 'footer.menu.careers', href: '#' },
      { labelKey: 'footer.menu.press_media', href: '#' },
    ],
  },
  {
    id: 'partners',
    type: 'links',
    titleKey: 'footer.partners',
    items: [
      { labelKey: 'footer.menu.become_partner', href: '#' },
      { labelKey: 'footer.menu.sponsorship', href: '#' },
      { labelKey: 'footer.menu.partner_directory', href: '#' },
      { labelKey: 'footer.menu.partner_login', href: '#' },
    ],
  },
  {
    id: 'contact',
    type: 'contact',
    titleKey: 'common.contact',
  },
];

export const FOOTER_BOTTOM_LINKS = [
  { labelKey: 'footer.links.privacy_policy', href: '#' },
  { labelKey: 'footer.links.terms_of_use', href: '#' },
] as const;
