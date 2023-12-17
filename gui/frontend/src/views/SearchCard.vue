<template>
  <div id="searchCard">
    <!-- <h3>
      カード検索<span>【 {{ this.page }} 】</span>
    </h3> -->
    <Title title="カード検索" />
    <h3>【 {{ this.page }} 】</h3>
    <!-- ボタン -->
    <div class="buttonWrapper">
      <move-button name="メニュー" />
      <!-- 変更ボタン -->
      <div>
        <button class="btn" @click="changeCategoryMode('top')">TOP</button>
        <button class="btn" @click="changeCategoryMode('compare')">
          エキスパンション比較
        </button>
        <button class="btn" @click="changeCategoryMode('insert')">登録</button>
      </div>
    </div>

    <div>
      <!-- ホーム画面テーブル -->
      <div :style="topHotStyle">
        <!-- formボタン -->
        <div class="formWrapper">
          <!-- 送信用ボタン -->
          <div>
            <!-- @submit.preventは、Enterキーによる送信を防止するため -->
            <form @submit.prevent="_loadData">
              <button class="btn" type="submit" :style="submitButtonStyle">
                検索
              </button>
              <input
                type="hidden"
                name="_csrf"
                value="5e5f0472-c675-43ad-be0a-d673a0325db4"
              />
            </form>
          </div>
          <div>
            <button class="btn" @click="clear">クリア</button>
          </div>
        </div>
        <!-- エラーメッセージ -->
        <div>
          <h3 class="errorMessage" v-if="errorMessage != ''">
            {{ errorMessage }}
          </h3>
        </div>
        <hot-table
          :settings="topSetting"
          ref="topHot"
          width="100%"
          height="calc(85vh - 250px)"
        ></hot-table>
      </div>
      <!-- 比較用テーブル -->
      <div :style="compareHotStyle">
        <hot-table
          :settings="compareSetting"
          ref="compareHot"
          width="100%"
          height="calc(85vh - 250px)"
        ></hot-table>
      </div>
      <!-- 登録用テーブル -->
      <div :style="insertHotStyle">
        <!-- 送信用ボタン -->
        <div class="formWrapper">
          <div>
            <!-- @submit.preventは、Enterキーによる送信を防止するため -->
            <form @submit.prevent="_save">
              <button class="btn" type="submit" :style="submitButtonStyle">
                登録
              </button>
              <input
                type="hidden"
                name="_csrf"
                value="5e5f0472-c675-43ad-be0a-d673a0325db4"
              />
            </form>
          </div>
          <div>
            <button class="btn" @click="clear">クリア</button>
          </div>
        </div>

        <!-- エラーメッセージ -->
        <div>
          <h3 class="errorMessage" v-if="errorMessage != ''">
            {{ errorMessage }}
          </h3>
        </div>
        <hot-table
          :settings="insertSetting"
          ref="insertHot"
          width="100%"
          height="calc(85vh - 250px)"
        ></hot-table>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
// ライブラリ
import Vue from "vue";
import { Component } from "vue-property-decorator";
import axios from "axios";

// コンポーネント
import { HotTable, HotColumn } from "@handsontable/vue";
import MoveButton from "../components/Common/MoveButton.vue";
import Title from "../components/Common/Title.vue";

// Hotセッティングメソッド
import { getSettingHeader } from "../setting/setting";
import {
  topHotSettings,
  compareHotSettings,
  insertHotSettings,
} from "../setting/setting";
import { contextMenuSetting } from "../setting/setting";

// 型定義
import type {
  BaseDataType,
  InsertResponseType,
  ResponseDataType,
} from "../type/setting";

// API
import { searchPriceCard } from "../common/api/common";

@Component({
  components: {
    HotTable,
    HotColumn,
    MoveButton,
    Title,
  },
})
class SearchCard extends Vue {
  // カテゴリ
  private readonly CATEGORY_TOP = "TOP";
  private readonly CATEGORY_COMPARE = "COMPARE";
  private readonly CATEGORY_INSERT = "INSERT";
  private categoryMode: "TOP" | "COMPARE" | "INSERT" = this.CATEGORY_TOP;

  // 未入力データメッセージ
  private NULL_DATA_MESSAGES: string[] = [];

  // 検索結果0件エラー
  private NO_RESULT = "データが取得できませんでした。";
  // 取得結果0件リスト
  errorCards = "";
  errorMessage = "";

  // 比較用データ格納リスト変数
  expansionResultList: BaseDataType[] = [];

  // 表示ページ
  page = "TOP";

  // TOP画面設定
  topSetting = {
    // データ初期値
    data: [],
    // ヘッダー設定
    colHeaders: getSettingHeader(topHotSettings),
    // 行ヘッダーの表示
    rowHeaders: true,
    // カラム設定
    columns: topHotSettings,
    // 主導行幅調整可否
    manualColumnResize: true,
    // 主導列調整可否
    manualRowResize: true,
    // 右クリックメニュー設定
    contextMenu: contextMenuSetting,
    // ライセンスキー（※無償版の場合は以下を指定すること）
    licenseKey: "non-commercial-and-evaluation",
  };

  // 比較画面設定
  compareSetting = {
    data: [],
    colHeaders: getSettingHeader(compareHotSettings),
    rowHeaders: true,
    columns: compareHotSettings,
    manualColumnResize: true,
    manualRowResize: true,
    contextMenu: contextMenuSetting,
    licenseKey: "non-commercial-and-evaluation",
  };

  // 登録画面設定
  insertSetting = {
    data: [],
    colHeaders: getSettingHeader(insertHotSettings),
    rowHeaders: true,
    columns: insertHotSettings,
    manualColumnResize: true,
    manualRowResize: true,
    contextMenu: contextMenuSetting,
    licenseKey: "non-commercial-and-evaluation",
  };

  /**
   * createdでは、DOMの処理が未完了の間に動いてしまう。
   * ページやデータの量が大きければcreatedでも良いが、画面描画時にrefの処理をするときは、mountedの方が良い。
   */
  // async mounted() {}

  // Data読み込み
  async _loadData(): Promise<void> {
    // エラーメッセージの初期化
    this.errorMessage = "";
    // エラーリストの初期化
    this.errorCards = "";
    // 比較用データリストの初期化
    this.expansionResultList = [];

    // TOP Hands On Table
    /* eslint @typescript-eslint/no-explicit-any: 0 */
    const _ref = await (this.$refs.topHot as any).hotInstance;

    // Compare Hands On Table
    const _compareRef = await (this.$refs.compareHot as any).hotInstance;

    // テーブル内のデータを取得
    const dataArrays = _ref.getData();

    // データをオブジェクトに変換
    const datas: BaseDataType[] = [];

    if (dataArrays.length === 0) {
      window.alert("データを入力してください");
      return;
    } else {
      for (const array of dataArrays) {
        const name: string = array[0];
        const cardType: string = array[1];
        const type: string = array[2];
        const expansion: string = array[3];
        const collection: string = array[4];
        const rarity: string = array[5];
        const promo: "有" | "無" = array[6];

        const newObj: BaseDataType = {
          name,
          cardType,
          type,
          expansion,
          collection,
          rarity,
          promo,
        };

        // データの未入力判定
        this.checkValidateData(newObj, this.NULL_DATA_MESSAGES);

        // エラーメッセージの確認。エラーがあれば処理を中断。
        if (this.NULL_DATA_MESSAGES.length !== 0) {
          this.showErrorMessage(this.NULL_DATA_MESSAGES);
          break;
        } else {
          datas.push(newObj);
        }
      }
      console.log(datas);

      // APIの呼び出し
      try {
        const response = await searchPriceCard(datas);
        console.log(response);

        const responseData: ResponseDataType = response.data;
        console.log(responseData);

        // 金額、expansion取得結果
        const { resultPrice, resultExpansion } = responseData;

        this.expansionResultList = resultExpansion;

        /**
         * 金額の取得結果を確認。
         * 金額がない場合は、エラーリストにカード名を追加。
         */
        for (const result of resultPrice) {
          if (result.price === null) {
            this.errorCards += result.name;
          }
        }

        if (this.errorCards !== "") {
          this.errorMessage = this.NO_RESULT + this.errorCards;
        }

        _ref.loadData(resultPrice);
        _compareRef.loadData(this.expansionResultList);
      } catch (e) {
        console.log(e.message);
      }
    }
  }

  // 保存
  async _save(): Promise<void> {
    // エラーメッセージの初期化
    this.errorMessage = "";
    // エラーリストの初期化
    this.errorCards = "";

    // 登録用テーブルのデータ取得
    const _ref = await (this.$refs.insertHot as any).hotInstance;

    console.log(_ref);
    console.log(_ref.getData());

    // テーブル内のデータを取得
    const dataArrays = _ref.getData();

    // データをオブジェクトに変換
    const datas = [];

    if (dataArrays.length === 0) {
      window.alert("データを入力してください");
      return;
    } else {
      for (const array of dataArrays) {
        const name: string = array[0];
        const cardType: string = array[1];
        const type: string = array[2];
        const expansion: string = array[3];
        const collection: string = array[4];
        const rarity: string = array[5];
        const promo: "有" | "無" = array[6];
        const price: number = array[7];

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

        // データの未入力判定
        this.checkValidateData(newObj, this.NULL_DATA_MESSAGES);

        // エラーメッセージの確認。エラーがあれば処理を中断。
        if (this.NULL_DATA_MESSAGES.length !== 0) {
          this.showErrorMessage(this.NULL_DATA_MESSAGES);
        } else {
          datas.push(newObj);
        }
      }
      console.log(datas);

      // APIの呼び出し
      try {
        const response = await axios.post(
          "http://localhost:8080/hot/add",
          datas
        );
        console.log(response);

        const responseData: InsertResponseType = response.data;
        console.log(responseData);
        const { statusType } = responseData;
        if (statusType === "SUCCESS") {
          window.alert(statusType);
        } else {
          window.alert("登録処理が異常終了しました。確認してください。");
        }
      } catch (e) {
        window.alert(e.message);
      }
    }
  }

  // データの未入力判定
  public checkValidateData(
    dataObj: BaseDataType,
    nullDataMessages: string[]
  ): void {
    const { name, cardType, expansion, collection, rarity } = dataObj;

    // エラーメッセージの中身を一度初期化
    if (nullDataMessages.length !== 0) {
      nullDataMessages.splice(0);
    }

    if (name === undefined || name === null) {
      nullDataMessages.push("カード名");
    }
    if (cardType === undefined || cardType === null) {
      nullDataMessages.push("カード種別");
    }
    if (expansion === undefined || expansion === null) {
      nullDataMessages.push("エクスパンション");
    }
    if (collection === undefined || collection === null) {
      nullDataMessages.push("コレクション");
    }
    if (rarity === undefined || rarity === null) {
      nullDataMessages.push("レアリティ");
    }
  }

  // エラーメッセージアラート
  public showErrorMessage(nullDataMessages: string[]) {
    const errorMessage = nullDataMessages.join();
    window.alert(errorMessage + "は必須項目です。入力してください。");
  }

  // カテゴリ変換
  async changeCategoryMode(
    categoryMode: "top" | "compare" | "insert"
  ): Promise<void> {
    if (categoryMode === "top") {
      this.categoryMode = this.CATEGORY_TOP;
      this.page = "TOP";
    } else if (categoryMode === "compare") {
      this.categoryMode = this.CATEGORY_COMPARE;
      this.page = "COMPARE";
    } else {
      this.categoryMode = this.CATEGORY_INSERT;
      this.page = "INSERT";
    }
  }

  async clear(): Promise<void> {
    if (this.categoryMode === "TOP") {
      /* eslint @typescript-eslint/no-explicit-any: 0 */
      const _ref = await (this.$refs.topHot as any).hotInstance;
      _ref.clear();
      _ref.loadData([]);
    } else if (this.categoryMode === "COMPARE") {
      const _compareRef = await (this.$refs.compareHot as any).hotInstance;
      _compareRef.clear();
      _compareRef.loadData([]);
    } else {
      const _insertRef = await (this.$refs.insertHot as any).hotInstance;
      _insertRef.clear();
      _insertRef.loadData([]);
    }
  }

  // TOPテーブルの表示切り替え
  get topHotStyle() {
    return this.categoryMode === this.CATEGORY_TOP ? false : "display: none;";
  }

  // 詳細テーブルの表示切り替え
  get compareHotStyle() {
    return this.categoryMode === this.CATEGORY_COMPARE
      ? false
      : "display: none;";
  }

  // 登録テーブルの表示切り替え
  get insertHotStyle() {
    return this.categoryMode === this.CATEGORY_INSERT
      ? false
      : "display: none;";
  }

  // 送信ボタンの表示切り替え
  get submitButtonStyle() {
    if (
      this.categoryMode === this.CATEGORY_TOP ||
      this.categoryMode === this.CATEGORY_INSERT
    ) {
      return false;
    } else {
      return "display: none";
    }
  }
}
export default SearchCard;
</script>

<style scoped>
::v-deep .handsontable .htDimmed {
  color: #333;
  background-color: #ddd;
  margin: 0 auto;
}

::v-deep .handsontable th {
  color: white;
  background-color: #0080b5;
}

#searchCard > h3 {
  padding: 5px 0;
  text-align: center;
}

.buttonWrapper,
.formWrapper {
  width: 100%;
  display: flex;
  justify-content: space-around;
  margin: 20px 0;
}

.formWrapper {
  margin: 30px 0;
}

.errorMessage {
  color: red;
  margin: 10px 0;
}
</style>
