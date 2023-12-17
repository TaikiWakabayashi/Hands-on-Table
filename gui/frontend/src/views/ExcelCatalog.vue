<template>
  <div id="excelCatalog">
    <Title title="エクセルアップロード" />
    <!-- メニュー & 画面切り替えボタン -->
    <div class="buttonWrapper">
      <move-button />
      <div>
        <!-- 登録内容確認遷移 -->
        <button class="btn" @click="changeCategoryMode('status')">
          アップロードステータス
        </button>
        <!-- 登録内容確認遷移 -->
        <button class="btn" @click="changeCategoryMode('insert')">
          内容確認・登録実行
        </button>
      </div>
    </div>

    <!-- アップロードボタン & Hot -->
    <div>
      <div :style="statusHotStyle">
        <div class="buttonWrapper">
          <!-- ファイルアップロードボタン -->
          <file-upload-button @upload="onFileUpload" ref="uploadButton" />
          <div>
            <button class="btn" @click="allClear">クリア</button>
          </div>
        </div>
        <!-- ステータステーブル -->
        <hot-table
          :settings="statusSettings"
          ref="uploadStatusHot"
          width="100%"
          height="calc(85vh - 250px)"
        ></hot-table>
      </div>
      <div :style="insertHotStyle">
        <!-- 登録送信ボタン -->
        <submit-button @insert="_save" @clear="allClear" />
        <!-- 登録内容確認テーブル -->
        <hot-table
          :settings="excelInsertSettings"
          ref="uploadInsertDataHot"
          width="100%"
          height="calc(85vh - 250px)"
        ></hot-table>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { HotColumn, HotTable } from "@handsontable/vue";
import Vue from "vue";
import { Component } from "vue-property-decorator";
import FileUploadButton from "../components/ExcelCatalog/button/FileUploadButton.vue";
import SubmitButton from "../components/ExcelCatalog/button/SubmitButton.vue";
import MoveBUtton from "../components/Common/MoveButton.vue";
import {
  getSettingHeader,
  excelCatalogSettings,
  contextMenuSetting,
  excelInsertHotSettings,
} from "../setting/setting";

import type { BaseDataType, InsertResponseType } from "../type/setting";
import * as XLSX from "xlsx";
import axios from "axios";
import Title from "../components/Common/Title.vue";

@Component({
  components: {
    HotTable,
    HotColumn,
    FileUploadButton,
    SubmitButton,
    MoveBUtton,
    Title,
  },
})
class ExcelCatalog extends Vue {
  data = [];
  // アップロードステータステーブル設定
  statusSettings = {
    data: [],
    colHeaders: getSettingHeader(excelCatalogSettings),
    rowHeaders: false,
    columns: excelCatalogSettings,
    manualColumnResize: true,
    manualRowResize: true,
    contextMenu: contextMenuSetting,
    licenseKey: "non-commercial-and-evaluation",
  };
  // エクセルアップロード内容確認用テーブル設定
  excelInsertSettings = {
    data: [],
    colHeaders: getSettingHeader(excelInsertHotSettings),
    rowHeaders: false,
    columns: excelInsertHotSettings,
    manualColumnResize: true,
    manualRowResize: true,
    contextMenu: contextMenuSetting,
    licenseKey: "non-commercial-and-evaluation",
  };

  // 登録データ
  datas: BaseDataType[] = [];

  // 未入力データメッセージ
  private NULL_DATA_MESSAGES: string[] = [];

  // カテゴリ
  private readonly CATEGORY_STATUS = "STATUS";
  private readonly CATEGORY_INSERT = "INSERT";
  private categoryMode: "STATUS" | "INSERT" = this.CATEGORY_STATUS;

  // アップロード時に実行
  async onFileUpload(value: File) {
    // 子コンポーネントからファイルをpropsで受け取り
    const file = value;
    if (file == undefined) {
      console.log("ファイルなし");
      return "";
    }
    console.log(file);
    const fileName = file.name;
    const startDateStr = new Date().toDateString();
    const startTimeStr = new Date().toTimeString();

    // エクセル内のデータを加工して受け取り
    await this.parseSheet(file, this.datas);

    console.log("this.data");
    console.log(this.datas);
    /* eslint @typescript-eslint/no-explicit-any: 0 */
    const _uploadStatusRef = await (this.$refs.uploadStatusHot as any)
      .hotInstance;
    /* eslint @typescript-eslint/no-explicit-any: 0 */
    const _excelInsertDataRef = await (this.$refs.uploadInsertDataHot as any)
      .hotInstance;

    const status = {
      check: false,
      fileName,
      rev: 1,
      download: "",
      newUploadDateAndTime: startDateStr + startTimeStr,
      version: 1,
    };

    await _uploadStatusRef.loadData(status);
    await _excelInsertDataRef.loadData(this.datas);
  }

  // エクセル内データの取得＋画面表示用にデータ加工
  async parseSheet(file: File, datas: BaseDataType[]): Promise<void> {
    await file.arrayBuffer().then((buffer) => {
      const workbook = XLSX.read(buffer, { type: "buffer" });
      const firstSheetName = workbook.SheetNames[0];
      const worksheet = workbook.Sheets[firstSheetName];
      const sheets = XLSX.utils.sheet_to_json(worksheet, {
        header: 2,
      });

      for (const rows of sheets) {
        const name: string = rows["カード名"];
        const cardType: string = rows["カード種別"];
        const type: string = rows["タイプ"];
        const expansion: string = rows["エキスパンション"];
        const collection: string = rows["コレクション"];
        const rarity: string = rows["レアリティ"];
        const promo: "有" | "無" = rows["プロモ有無"];
        const price: number = rows["金額"];

        const newObj: BaseDataType = {
          name,
          cardType,
          type,
          expansion,
          collection,
          rarity,
          promo,
          price,
        };
        datas.push(newObj);
      }
      console.log(datas);
    });
  }

  async _save() {
    try {
      if (this.datas.length === 0) {
        throw Error("登録するデータが存在しません。");
      } else {
        const response = await axios.post(
          "http://localhost:8080/hot/add",
          this.datas
        );

        console.log(response);
        const responseData: InsertResponseType = response.data;
        const { statusType } = responseData;
        if (statusType === "SUCCESS") {
          /* eslint @typescript-eslint/no-explicit-any: 0 */
          const _excelInsertDataRef = await (
            this.$refs.uploadInsertDataHot as any
          ).hotInstance;
          _excelInsertDataRef.clear();
          window.alert(statusType);
        } else {
          window.alert("登録処理が異常終了しました。確認してください。");
        }
      }
    } catch (e) {
      window.alert(e.message);
    }
  }

  // カテゴリ変換
  async changeCategoryMode(categoryMode: "status" | "insert"): Promise<void> {
    if (categoryMode === "status") {
      this.categoryMode = this.CATEGORY_STATUS;
    } else {
      this.categoryMode = this.CATEGORY_INSERT;
    }
  }

  // ステータステーブルの表示切り替え
  get statusHotStyle() {
    return this.categoryMode === this.CATEGORY_STATUS
      ? false
      : "display: none;";
  }

  // 詳細テーブルの表示切り替え
  get insertHotStyle() {
    return this.categoryMode === this.CATEGORY_INSERT
      ? false
      : "display: none;";
  }

  // アップロードデータの削除
  async allClear(): Promise<void> {
    /* eslint @typescript-eslint/no-explicit-any: 0 */
    const _ref = await (this.$refs.uploadStatusHot as any).hotInstance;
    _ref.loadData([]);

    const _compareRef = await (this.$refs.uploadInsertDataHot as any)
      .hotInstance;
    _compareRef.loadData([]);
  }
}
export default ExcelCatalog;
</script>

<style scoped>
::v-deep .handsontable .htDimmed {
  color: #333;
  background-color: #ddd;
}

::v-deep .handsontable .htCheckboxRendererInput {
  margin: 0 !important;
}

::v-deep .handsontable th {
  color: white;
  background-color: #0080b5;
}

::v-deep .handsontable td {
  height: unset !important;
  line-height: unset !important;
  vertical-align: inherit !important;
}

.htCenter {
  text-align: center;
  line-height: inherit;
}

.buttonWrapper {
  width: 100%;
  display: flex;
  justify-content: space-around;
  margin: 20px 0;
}

/* @media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
} */
</style>
