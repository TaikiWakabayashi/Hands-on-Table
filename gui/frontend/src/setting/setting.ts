import type { HotSettingHeader } from "@/type/setting";

/**
 * 初期表示テーブル設定
 */
export const topHotSettings: HotSettingHeader[] = [
  { data: "name", width: 300, header: "カード名" },
  {
    data: "cardType",
    width: 200,
    header: "カード種別",
    editor: "select",
    selectOptions: [
      "モンスター",
      "サポート",
      "グッズ",
      "スタジアム",
      "ポケモンのどうぐ",
      "基本エネルギー",
      "特殊エネルギー",
    ],
  },
  {
    data: "type",
    width: 100,
    header: "タイプ",
    editor: "select",
    selectOptions: [
      "炎",
      "草",
      "水",
      "雷",
      "闘",
      "超",
      "ドラゴン",
      "フェアリー",
      "悪",
      "鋼",
      "無職",
      "-",
    ],
  },
  { data: "expansion", width: 200, header: "エキスパンション" },
  { data: "collection", width: 150, header: "コレクション" },
  { data: "rarity", width: 80, header: "レアリティ" },
  {
    data: "promo",
    width: 100,
    header: "プロモ有無",
    editor: "select",
    selectOptions: ["有", "無"],
  },
  { data: "price", width: 150, header: "金額", readOnly: true },
];

/**
 * 比較用テーブル設定
 */
export const compareHotSettings: HotSettingHeader[] = [
  { data: "name", width: 300, header: "カード名", readOnly: true },
  { data: "cardType", width: 200, header: "カード種別", readOnly: true },
  { data: "type", width: 100, header: "タイプ", readOnly: true },
  { data: "expansion", width: 200, header: "エキスパンション", readOnly: true },
  { data: "rarity", width: 80, header: "レアリティ", readOnly: true },
  { data: "price", width: 150, header: "金額", readOnly: true },
];

/**
 * 登録用テーブル設定
 */
export const insertHotSettings: HotSettingHeader[] = [
  { data: "name", width: 300, header: "カード名" },
  {
    data: "cardType",
    width: 200,
    header: "カード種別",
    editor: "select",
    selectOptions: [
      "モンスター",
      "サポート",
      "グッズ",
      "スタジアム",
      "ポケモンのどうぐ",
      "基本エネルギー",
      "特殊エネルギー",
    ],
  },
  {
    data: "type",
    width: 100,
    header: "タイプ",
    editor: "select",
    selectOptions: [
      "炎",
      "草",
      "水",
      "雷",
      "闘",
      "超",
      "ドラゴン",
      "フェアリー",
      "悪",
      "鋼",
      "無職",
      "-",
    ],
  },
  { data: "expansion", width: 200, header: "エキスパンション" },
  { data: "collection", width: 150, header: "コレクション" },
  { data: "rarity", width: 80, header: "レアリティ" },
  {
    data: "promo",
    width: 100,
    header: "プロモ有無",
    editor: "select",
    selectOptions: ["有", "無"],
  },
  { data: "price", width: 150, header: "金額" },
];

// ヘッダー設定取得用メソッド
export function getSettingHeader(settings: HotSettingHeader[]): string[] {
  const response: string[] = [];
  for (const setting of settings) {
    if (setting.header != undefined) {
      response.push(setting.header);
    }
  }
  return response;
}

// ContextMenu設定
export const contextMenuSetting = {
  items: {
    row_above: {
      name: "上に行を挿入",
    },
    row_below: {
      name: "下に行を挿入",
    },
    col_left: {
      name: "左側に列を挿入",
    },
    col_right: {
      name: "右側に列を挿入",
    },
    remove_row: {
      name: "行を削除",
    },
    clear_colmn: {
      name: "列のデータを削除",
    },
    undo: {
      name: "元に戻す",
    },
    redo: {
      name: "やり直す",
    },
    cut: {
      name: "切り取り",
    },
    copy: {
      name: "コピー",
    },
  },
};

// ExcelCatalog status用テーブル
export const excelCatalogSettings: HotSettingHeader[] = [
  {
    data: "check",
    width: 70,
    header: "",
    type: "checkbox",
    className: "htCenter",
  },
  { data: "fileName", width: 150, header: "ファイル名", readOnly: true },
  { data: "rev", width: 100, header: "Rev", readOnly: true },
  { data: "download", width: 150, header: "ダウンロード", readOnly: true },
  {
    data: "newUploadDateAndTime",
    width: 200,
    header: "最新アップロード日時",
    readOnly: true,
  },
  { data: "version", width: 150, header: "バージョン", readOnly: true },
];

/**
 * Excelアップロード用登録テーブル設定
 */
export const excelInsertHotSettings: HotSettingHeader[] = [
  { data: "name", width: 300, header: "カード名", readOnly: true },
  { data: "cardType", width: 200, header: "カード種別", readOnly: true },
  { data: "type", width: 100, header: "タイプ", readOnly: true },
  { data: "expansion", width: 200, header: "エキスパンション", readOnly: true },
  { data: "collection", width: 150, header: "コレクション", readOnly: true },
  { data: "rarity", width: 80, header: "レアリティ", readOnly: true },
  { data: "promo", width: 100, header: "プロモ有無", readOnly: true },
  { data: "price", width: 150, header: "金額", readOnly: true },
];

/**
 * 手配カード選択テーブル設定
 */
export const arrangeCardHotSettings: HotSettingHeader[] = [
  { data: "check", type: "checkbox", width: 50, header: "" },
  { data: "name", width: 160, header: "カード名", readOnly: true },
  { data: "type", width: 100, header: "タイプ", readOnly: true },
  { data: "expansion", width: 150, header: "エキスパンション", readOnly: true },
  { data: "collection", width: 150, header: "コレクション", readOnly: true },
  { data: "rarity", width: 120, header: "レアリティ", readOnly: true },
  { data: "promo", width: 120, header: "プロモ有無", readOnly: true },
  { data: "price", width: 120, header: "金額", readOnly: true },
];

/**
 * 手配カード一覧テーブル設定
 */
export const arrangeCardListSettings: HotSettingHeader[] = [
  { data: "check", type: "checkbox", width: 50, header: "" },
  { data: "name", width: 160, header: "カード名", readOnly: true },
  { data: "cardType", width: 140, header: "カード種別", readOnly: true },
  { data: "type", width: 100, header: "タイプ", readOnly: true },
  { data: "expansion", width: 150, header: "エキスパンション", readOnly: true },
  { data: "collection", width: 150, header: "コレクション", readOnly: true },
  { data: "rarity", width: 120, header: "レアリティ", readOnly: true },
  { data: "promo", width: 120, header: "プロモ有無", readOnly: true },
  { data: "price", width: 120, header: "金額", readOnly: true },
];
