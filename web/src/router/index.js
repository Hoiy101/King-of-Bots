import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '@/views/pk/PkIndexView.vue'
import RanKlistIndexView from '@/views/ranklist/RanKlistIndexView.vue'
import RecordIndexView from '@/views/record/RecordIndexView.vue'
import UserBotIndexView from '@/views/user/bot/UserBotIndexView.vue'
import NotFound from '@/views/error/NotFound.vue'
import UserSettingsVies from '@/views/user/bot/settings/UserSettingsVies.vue'
import UserAccountLoginView from '@/views/user/bot/account/UserAccountLoginView.vue'
import UserAccountRegisterView from '@/views/user/bot/account/UserAccountRegisterView.vue'
const routes = [
  {
    path:"/",
    name:"home",
    component:PkIndexView,
  },
  {
    path:"/pk/",
    name:"pk_index",
    component:PkIndexView,
  },
  {
    path:"/ranklist/",
    name:"RanKlist_index",
    component:RanKlistIndexView,
  },
  {
    path:"/record/",
    name:"record_index",
    component:RecordIndexView,
  },
  {
    path:"/user/bot/",
    name:"userbot_index",
    component:UserBotIndexView,
  },
  {
    path:"/user/settings/",
    name:"settings_index",
    component: UserSettingsVies,
  },
  {
    path:"/:CatchAll(.*)",
    name:"NotFound_index",
    component:NotFound,
  },
    {
    path:"/user/account/login/",
    name:"user_account_login",
    component:UserAccountLoginView,
  },
    {
    path:"/user/account/register/",
    name:"user_account_register",
    component:UserAccountRegisterView,
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes 
})

export default router
