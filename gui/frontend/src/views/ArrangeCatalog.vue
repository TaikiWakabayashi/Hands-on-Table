<template>
  <div id="arrangeCatalog">
    <Title title="カード手配" />
    <div class="container">
      <!-- ボタン -->
      <div class="buttonWrapper">
        <move-button />
        <div>
          <button class="btn" @click="addArrangement">手配追加</button>
          <button class="btn" @click="exportExcel">エクセル発行</button>
        </div>
      </div>

      <!-- 手配一覧テーブル -->
      <div class="tableLayout2 hotTable">
        <hot-table
          style="z-index: 5"
          width="100%"
          height="100%"
          :settings="arrangeCardListSettings"
          ref="arrangeListHot"
        ></hot-table>
      </div>

      <!-- 金額表示 -->
      <div class="priceArea">
        <h2>現在の金額：¥{{ this.price }}</h2>
      </div>
    </div>

    <!-- 手配追加モーダル -->
    <div class="greyArea" v-show="isModalOpen">
      <div class="wrapper">
        <title title="【 手配カード追加 】" />
        <div class="wrapper2">
          <!-- エラー -->
          <p v-show="isNoDataError" class="errorMsg">
            {{ this.DATA_IS_UNDEFINED_ERROR }}
          </p>
          <p v-show="isNoSelectCategoryError" class="errorMsg">
            {{ this.SELECT_CATEGORY_ERROR }}
          </p>
          <p v-show="isNoAddDataError" class="errorMsg">
            {{ this.NO_ADD_DATA }}
          </p>
          <div class="inputWrapper">
            <div class="inputWrapper2">
              <label class="label">カード種別</label>
              <select
                name="cardName"
                v-model="selectCardType"
                @change="changeCardType"
              >
                <option v-for="cardType of cardTypes" :key="cardType">
                  {{ cardType }}
                </option>
              </select>
              <label class="label2"
                >※ 手配するカード種別を選択してください。</label
              >
            </div>
            <div class="inputWrapper2">
              <input type="checkbox" @change="checkAll" />
              <label class="label2">全選択</label>
            </div>
          </div>
        </div>
        <!-- カード手配参照テーブル -->
        <div class="tableLayout hotTable">
          <hot-table
            style="z-index: 5"
            width="100%"
            height="100%"
            :settings="arrangeCardHotSettings"
            ref="arrangeCardHot"
          ></hot-table>
        </div>
        <div class="buttonWrapper2">
          <button class="btn" @click="add">追加</button>
          <button class="btn" @click="cancel">キャンセル</button>
        </div>
      </div>
    </div>

    <!-- Loadingモーダル -->
    <div class="greyArea2" v-show="isLoadingModalOpen">
      <div class="loadingWrapper">
        <p>Loading ...</p>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
// ライブラリ
import Vue from "vue";
import { HotColumn, HotTable } from "@handsontable/vue";
import { Component } from "vue-property-decorator";
import axios from "axios";

// コンポーネント
import MoveButton from "../components/Common/MoveButton.vue";
import Title from "../components/Common/Title.vue";

// Hotセッテイング
import {
  arrangeCardHotSettings,
  arrangeCardListSettings,
  getSettingHeader,
} from "../setting/setting";

// タイプ
import type { BaseDataType, Sheet } from "../type/setting";

// メソッド
import { convertToIndex, exportSheet } from "../common/api/common";

@Component({
  components: {
    HotTable,
    HotColumn,
    MoveButton,
    Title,
  },
})
class ArrangeCatalog extends Vue {
  // モーダル表示フラグ
  isModalOpen = false;
  // Loadingモーダル表示フラグ
  isLoadingModalOpen = false;

  // カードタイプ
  cardTypes = [
    "モンスター",
    "サポート",
    "グッズ",
    "スタジアム",
    "ポケモンのどうぐ",
    "基本エネルギー",
    "特殊エネルギー",
  ];

  // 手配カード選択テーブル設定
  arrangeCardHotSettings = {
    data: [],
    colHeaders: getSettingHeader(arrangeCardHotSettings),
    rowHeaders: false,
    filters: true,
    dropdownMenu: [
      "filter_by_condition",
      "filter_by_value",
      "filter_action_bar",
    ],
    columns: arrangeCardHotSettings,
    manualColumnResize: true,
    manualRowResize: true,
    licenseKey: "non-commercial-and-evaluation",
  };

  // 手配カード一覧テーブル設定
  arrangeCardListSettings = {
    data: [],
    colHeaders: getSettingHeader(arrangeCardListSettings),
    rowHeaders: false,
    filters: true,
    dropdownMenu: ["filter_by_value", "filter_action_bar"],
    columns: arrangeCardListSettings,
    manualColumnResize: true,
    manualRowResize: true,
    licenseKey: "non-commercial-and-evaluation",
  };

  // エラーフラグ
  isNoDataError = false;
  isNoSelectCategoryError = false;
  isNoAddDataError = false;

  // エラーメッセージ
  readonly DATA_IS_UNDEFINED_ERROR = "取得可能なデータがありません。";
  readonly SELECT_CATEGORY_ERROR = "カード種別を選択してください。";
  readonly NO_ADD_DATA =
    "追加するデータが選択されていないか、取得されたデータがありません。";

  // 選択したカードタイプ
  selectCardType = "";

  // 手配カード保存変数
  // private arrangeCardList: BaseDataType[] = [];

  // 合計金額
  price = 0;

  // 手配追加
  addArrangement() {
    this.isModalOpen = true;
    // 手配モーダルのエラーを初期化
    this.isNoDataError = false;
    this.isNoSelectCategoryError = false;
    this.isNoAddDataError = false;
  }

  // 追加
  async add(): Promise<void> {
    // カード種別選択なし
    if (this.selectCardType == "") {
      this.isNoSelectCategoryError = true;
      return;
    }
    // データなしで押下
    if (this.isNoDataError) {
      this.isNoAddDataError = true;
      return;
    }

    // 手配カード選択テーブルを取得
    /* eslint @typescript-eslint/no-explicit-any: 0 */
    const _ref = await (this.$refs.arrangeCardHot as any).hotInstance;

    // 手配カード一覧テーブル設定
    /* eslint @typescript-eslint/no-explicit-any: 0 */
    const _arrangeListRef = await (this.$refs.arrangeListHot as any)
      .hotInstance;

    // 合計金額の初期化
    this.price = 0;

    const getDataArray: any[] = _ref.getData();
    console.log(getDataArray);

    // 手配一覧に設定するデータ
    const selectData: BaseDataType[] = [];
    // 合計金額
    let price = 0;

    // 選択したデータを抽出
    for (const data of getDataArray) {
      if (data[0]) {
        const selectObj: BaseDataType = {
          check: false,
          name: data[1],
          cardType: this.selectCardType,
          type: data[2],
          expansion: data[3],
          collection: data[4],
          rarity: data[5],
          promo: data[6],
          price: data[7],
        };

        price += data[7];
        selectData.push(selectObj);

        // 手配追加モーダルオフ
        this.isModalOpen = false;
      }
    }

    console.log(selectData);
    this.price = price;
    _arrangeListRef.loadData(selectData);
  }

  // 手配キャンセル
  cancel() {
    this.isModalOpen = false;
    this.selectCardType = "";
  }

  async changeCardType() {
    console.log("選択カード種別", this.selectCardType);

    // Hotテーブルを取得
    /* eslint @typescript-eslint/no-explicit-any: 0 */
    const _ref = await (this.$refs.arrangeCardHot as any).hotInstance;

    // 既存データがある場合初期化
    if (_ref.getData().length !== 0) {
      _ref.loadData([]);
    }

    // Loadingモーダルオン
    this.isLoadingModalOpen = true;

    // エラーを一度初期化
    this.isNoDataError = false;
    this.isNoSelectCategoryError = false;
    this.isNoAddDataError = false;

    // 選択した値をパラメータに設定
    const params = {
      selectCardType: this.selectCardType,
    };

    const response = await axios
      .post("http://localhost:8080/hot/selectCard", params)
      .then((response) => {
        return response;
      })
      .finally(
        () =>
          // Loadingモーダルオフ
          (this.isLoadingModalOpen = false)
      );
    console.log(response);

    let customRes = [];

    if (response.data.length !== 0) {
      customRes = response.data.map((res) => {
        const newObj = { check: false, ...res };
        return newObj;
      });
    } else {
      this.isNoDataError = true;
    }

    console.log("customRes", customRes);

    _ref.loadData(customRes);
  }

  async exportExcel(): Promise<void> {
    const input: Sheet[] = [];
    // 表示データ配列格納配列
    const rowsData = [];
    // エクセルヘッダー定義配列
    const header: string[] = [];

    // 各行の変数定義
    const rowA = convertToIndex("A");
    const rowB = convertToIndex("B");
    const rowC = convertToIndex("C");
    const rowD = convertToIndex("D");
    const rowE = convertToIndex("E");
    const rowF = convertToIndex("F");
    const rowG = convertToIndex("G");
    const rowH = convertToIndex("H");

    // ヘッダーの定義
    header[rowA] = "カード名";
    header[rowB] = "カード種別";
    header[rowC] = "タイプ";
    header[rowD] = "エキスパンション";
    header[rowE] = "コレクション";
    header[rowF] = "レアリティ";
    header[rowG] = "プロモ有無";
    header[rowH] = "金額";

    // 手配カード一覧テーブル設定
    /* eslint @typescript-eslint/no-explicit-any: 0 */
    const _arrangeListRef = await (this.$refs.arrangeListHot as any)
      .hotInstance;

    // データ読み込み
    const getDataArray = _arrangeListRef.getData();

    // 手配一覧に設定するデータ
    const selectData: BaseDataType[] = [];

    // 選択したデータを抽出
    for (const data of getDataArray) {
      if (data[0]) {
        const selectObj: BaseDataType = {
          name: data[1],
          cardType: this.selectCardType,
          type: data[3],
          expansion: data[4],
          collection: data[5],
          rarity: data[6],
          promo: data[7],
          price: data[8],
        };

        selectData.push(selectObj);
      }
    }

    // 取得したデータ配列をループし、dataに値設定
    if (selectData.length !== 0) {
      for (const card of selectData) {
        // エクセル表示データ配列
        const data = [];
        data[rowA] = card.name;
        data[rowB] = card.cardType;
        data[rowC] = card.type;
        data[rowD] = card.expansion;
        data[rowE] = card.collection;
        data[rowF] = card.rarity;
        data[rowG] = card.promo;
        data[rowH] = card.price;

        console.log("data", data);
        rowsData.push(data);
        console.log(rowsData);
      }
    } else {
      window.alert("手配データがありません。");
      return;
    }

    console.log("rowsData", rowsData);

    // シートに設定
    const sheet: Sheet = {
      name: "カード一覧",
      header: header,
      data: rowsData,
    };
    input.push(sheet);

    // エクセルを出力
    exportSheet("カード手配一覧.xlsx", input);
    console.log(input);
  }

  async checkAll(checkbox) {
    // 手配カード選択テーブルを取得
    /* eslint @typescript-eslint/no-explicit-any: 0 */
    const hot = await (this.$refs.arrangeCardHot as any).hotInstance;

    const col = hot.propToCol("check");
    hot.populateFromArray(
      0,
      col,
      [[checkbox.target.checked]],
      hot.countRows() - 1,
      col,
      null,
      null,
      "down"
    );
  }
}

export default ArrangeCatalog;
</script>

<style scoped>
body {
  position: relative;
}

.container {
  width: 100%;
  max-width: 1000px;
  margin: 0 auto;
}

::v-deep .handsontable .htDimmed {
  color: #333;
  background-color: #ddd;
  margin: 0 auto;
}

::v-deep .handsontable th {
  color: white;
  background-color: #0080b5;
}

::v-deep .handsontable .htCheckboxRendererInput {
  text-align: center;
  display: inline-block;
}

.buttonWrapper {
  width: 100%;
  display: flex;
  justify-content: space-between;
  margin: 20px 0;
}

.buttonWrapper2 {
  width: 100%;
  max-width: 500px;
  display: flex;
  justify-content: space-around;
  margin: 30px auto 0 auto;
}

.greyArea {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 10;
  width: 100%;
  height: 100%;
  background-color: rgba(128, 128, 128, 0.5);
}

.greyArea2 {
  z-index: 20;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(95, 95, 95, 0.8);
}

.wrapper {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translateY(-50%) translateX(-50%);
  width: 100%;
  max-width: 1000px;
  height: 100%;
  max-height: 700px;
  background-color: white;
}

.wrapper2 {
  margin: 0 10px;
}

.loadingWrapper {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translateY(-50%) translateX(-50%);
  width: 100%;
  max-width: 700px;
  background-color: white;
  text-align: center;
  font-size: 15px;
  padding: 15px 0;
}

.inputWrapper {
  width: 100%;
  padding: 20px 0;
}

.inputWrapper2 {
  width: 100%;
  padding: 5px 0;
}

.tableLayout {
  padding: 1px;
  background: #666;
  height: 50vh;
  margin: 0 10px 15px 10px;
}

.tableLayout2 {
  width: 100%;
  max-width: 1000px;
  padding: 1px;
  background: rgba(93, 93, 93, 0.4);
  height: 50vh;
  margin: 0 auto;
}

.hotTable {
  position: relative;
  z-index: 0;
}

.errorMsg {
  color: red;
  font-weight: bold;
}

.label {
  margin-right: 15px;
}

.label2 {
  margin-left: 15px;
  color: #0080b5;
  font-size: 10px;
  vertical-align: text-top;
}

.priceArea {
  width: 100%;
  text-align: right;
  margin-top: 80px;
}

.priceArea > h2 {
  font-weight: bold;
  color: #cabd00;
}
</style>
