export type HotSettingHeader = {
  data?: string;
  width?: number;
  header?: string;
  readOnly?: boolean;
  editor?: string;
  selectOptions?: string[];
  type?: string;
  className?: string;
};

export type BaseDataType = {
  check?: boolean;
  name: string;
  cardType: string;
  type: string;
  expansion: string;
  collection: string;
  rarity: string;
  promo: "有" | "無";
  price?: number;
};

export type ResponseDataType = {
  resultPrice: BaseDataType[];
  resultExpansion: BaseDataType[];
};

export type InsertResponseType = {
  statusType: "SUCCESS" | "ABNORMAL";
};

export type Sheet = {
  name: string;
  header: string[];
  data: any[];
};
