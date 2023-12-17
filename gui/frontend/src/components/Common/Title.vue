<template>
  <div id="title">
    <h3>{{ title }}</h3>
  </div>
</template>

<script lang="ts">
import Vue from "vue";
import { Component, Prop } from "vue-property-decorator";

@Component({})
class Title extends Vue {
  @Prop()
  title: string;
}

export default Title;
</script>

<style scoped>
#title {
  width: 100%;
}

h3 {
  padding: 10px 0;
  text-align: center;
  font-weight: bold;
}
</style>
