<template>
  <div>
    <input
      id="uploadButton"
      type="file"
      accept=".xlsx"
      @input="onFileUpload"
      ref="fileUpload"
      style="display: none"
    />
    <button class="btn" type="button" @click="actionFileUpload">
      アップロード
    </button>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { Component } from "vue-property-decorator";

// $emitを利用する場合、子コンポーネントに@Componentをつけないと読み込まれないので注意。
@Component({})
class FileUploadButton extends Vue {
  onFileUpload(event: Event) {
    console.log("FileUploadButton : upload");
    console.log(event);
    const target = event.target as HTMLInputElement;
    const file = (target.files as FileList)[0];
    this.$emit("upload", file);
  }

  async actionFileUpload() {
    const uploadButton = document.getElementById("uploadButton");

    console.log(uploadButton);

    if (uploadButton) {
      uploadButton.click();
    }
  }
}

export default FileUploadButton;
</script>

<style scoped></style>
