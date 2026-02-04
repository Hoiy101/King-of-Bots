<template>
    <ContentField>
        我的Bot
    </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue';
import $ from 'jquery';
import { useStore } from 'vuex';

export default{
    components: {
        ContentField
    },
    setup(){
        const store = useStore();
        $.ajax({
            url: "http://127.0.0.1:3000/user/bot/getlist/",
            type: "get",
            headers: {
                Authorization : "Bearer " + store.state.user.token,
            },
            success(resp) {
                console.log(resp);
            },
            error(resp) {
                console.log(resp);
            },
        })
    }
}
</script>

<style scoped>
</style>