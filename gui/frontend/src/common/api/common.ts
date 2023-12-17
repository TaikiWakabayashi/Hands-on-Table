import axios from "axios";
import type { BaseDataType, Sheet } from "@/type/setting";
import * as XLSX from "xlsx";

export async function getToken(csrf: string) {
  const csrfToken = "";
  const response = await axios.get("http://localhost:8080/csrftoken", {
    headers: {
      _csrf: csrf,
    },
  });
  console.log(response);
}

export function createCsrf(
  firstName?: string,
  lastName?: string,
  pass?: string
): string {
  let _csrf = "";

  if (firstName !== undefined) {
    _csrf += firstName;
  } else if (lastName !== undefined) {
    _csrf += lastName;
  } else if (pass !== undefined) {
    _csrf += pass;
  } else {
    _csrf = "satoshi";
  }

  return _csrf;
}

// 金額データ取得API
export async function searchPriceCard(datas: BaseDataType[]) {
  const response = axios.post("http://localhost:8080/hot", datas);

  return response;
}

// 選択したカードタイプに該当するカードを取得
export async function searchCard(params: { [key: string]: string }) {
  const response = axios.post("http://localhost:8080/hot/selectCard", params);

  return response;
}

// デコード
export function convertToIndex(colName: string): number {
  return XLSX.utils.decode_col(colName);
}

// エクセル出力
export function exportSheet(fileName: string, input: Sheet[]) {
  const sheets: { [sheet: string]: XLSX.WorkSheet } = {};
  const sheetNames: string[] = [];

  for (const sheet of input) {
    const data: any[][] = [];
    if (sheet.header) {
      data.push(sheet.header);
    }

    for (const row of sheet.data) {
      data.push(row);
    }
    console.log(data);
    sheets[sheet.name] = XLSX.utils.aoa_to_sheet(data, {});
    sheetNames.push(sheet.name);
  }

  const wookBook: XLSX.WorkBook = {
    SheetNames: sheetNames,
    Sheets: sheets,
  };
  XLSX.writeFile(wookBook, fileName, {});
}
