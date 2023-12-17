<template>
  <div class="buttonWrapper">
    <!-- @submit.preventは、Enterキーによる送信を防止するため -->
    <form @submit.prevent="_save">
      <button class="btn" type="submit">登録</button>
      <input
        type="hidden"
        name="_csrf"
        value="5e5f0472-c675-43ad-be0a-d673a0325db4"
      />
    </form>
    <button class="btn" @click="clear">クリア</button>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { Component } from "vue-property-decorator";

// $emitを利用する場合、子コンポーネントに@Componentをつけないと読み込まれないので注意。
@Component({})
class SubmitButton extends Vue {
  _save(event: Event) {
    console.log("SubmitButton : upload");
    console.log(event);
    this.$emit("insert");
  }

  clear() {
    console.log("SubmitButton : clear");
    this.$emit("clear");
  }
}

export default SubmitButton;
</script>

<style scoped>
.buttonWrapper {
  width: 100%;
  display: flex;
  justify-content: space-around;
  margin: 20px 0;
}
</style>
