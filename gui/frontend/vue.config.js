module.exports = {
  devServer: {
    Proxy: {
      "http://localhost:5173": {
        target: "http://localhost:8080",
      },
    },
  },
  Plugin: [
    {
      src: "@/plugins/vue-js-xlsx.js",
      ssr: false,
    },
  ],
};
