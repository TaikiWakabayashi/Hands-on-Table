<template>
  <div id="mainMenu">
    <title title="メインメニュー" />
    <!-- ボタン -->
    <div class="wrapper">
      <move-button name="カード検索" />
      <move-button name="エクセルアップロード" />
      <move-button name="カード手配" />
    </div>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import Component from "vue-class-component";
import MoveBUtton from "../components/Common/MoveButton.vue";
import Title from "../components/Common/Title.vue";

@Component({
  components: {
    MoveBUtton,
    Title,
  },
})
class MainMenu extends Vue {}
export default MainMenu;
</script>

<style scoped>
#mainMenu {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  flex-direction: column;
}

.wrapper {
  width: 100%;
  /* height: 100%; */
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 20px 0;
}
</style>
