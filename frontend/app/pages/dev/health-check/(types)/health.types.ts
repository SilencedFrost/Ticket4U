export interface ecJWK {
  kty: string;
  crv: string;
  kid: string;
  x: string;
  y: string;
  alg: string;
}

export interface userPrincipal {
  userId: string;
  authorities: string;
}

export const emptyJwk: ecJWK = {
  kty: '',
  crv: '',
  kid: '',
  x: '',
  y: '',
  alg: '',
};

export const emptyUser: userPrincipal = {
  userId: '',
  authorities: '',
};
