import Vue from "vue";
import VueRouter from "vue-router";
import HomeView from "../views/HomeView.vue";

Vue.use(VueRouter);

const router = new VueRouter({
  mode: "history",
  base: import.meta.env.BASE_URL,
  routes: [
    // {
    //   path: "/home",
    //   name: "home",
    //   component: HomeView,
    // },
    {
      path: "/",
      name: "MainMenu",
      component: () => import("../views/MainMenu.vue"),
    },
    {
      path: "/searchCard",
      name: "SearchCard",
      component: () => import("../views/SearchCard.vue"),
    },
    {
      path: "/excelCatalog",
      name: "ExcelCatalog",
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import("../views/ExcelCatalog.vue"),
    },
    {
      path: "/arrangeCatalog",
      name: "ArrangeCatalog",
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import("../views/ArrangeCatalog.vue"),
    },
  ],
});

export default router;
