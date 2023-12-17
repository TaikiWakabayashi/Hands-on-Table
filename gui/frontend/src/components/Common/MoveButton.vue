<template>
  <div>
    <button class="btn" @click="movePage(name)">{{ name }}</button>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { Component, Prop } from "vue-property-decorator";

@Component({
  components: {},
})
class MoveBUtton extends Vue {
  @Prop({ default: "メニュー" })
  name: string;

  // 指定したページに移動
  public async movePage(movePageName: string): Promise<void> {
    console.log("遷移");
    console.log(movePageName);

    switch (movePageName) {
      // カード検索
      case "メニュー":
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        await this.$router.push("/");
        break;

      // カード検索
      case "カード検索":
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        await this.$router.push("searchCard");
        break;

      // エクセルページ遷移
      case "エクセルアップロード":
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        await this.$router.push("excelCatalog");
        break;

      // 手配一覧ページ遷移
      case "カード手配":
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        await this.$router.push("arrangeCatalog");
        break;

      // その他（エラー回避のため）
      default:
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-ignore
        await this.$router.push("/");
        break;
    }
  }
}

export default MoveBUtton;
</script>

<style scoped></style>
