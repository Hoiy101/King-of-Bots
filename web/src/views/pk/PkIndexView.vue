<template>
     <PlayGround v-if="$store.state.pk.status === 'playing'"/>
     <MatchGround v-else-if="$store.state.pk.status === 'matching'"/>
</template>

<script>
import PlayGround from '@/components/PlayGround.vue';
import MatchGround from '@/components/MatchGround.vue';
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default{
    components: {
        PlayGround,
        MatchGround,
    },
    setup(){
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;

        let socket = null;
        onMounted(() => {
            store.commit("updateOpponent",{
                username: "你的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            })
            socket = new WebSocket(socketUrl);
            socket.onopen = () => {
                console.log("WebSocket连接已打开");
                store.commit("updateSocket", socket);
            }
            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                if(data.event === "start-matching"){
                    store.commit("updateOpponent",{
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    })
                    store.commit("updateGamemap", data.gamemap);
                    setTimeout(() =>{
                        store.commit("updatestatus", "playing");  
                    }, 2000);              
                }
            }
            socket.onclose = () => {
                console.log("WebSocket连接已关闭");
                store.commit("updatestatus", "matching");  
            }
        });
        onUnmounted(() => {
            socket.close();
        })
    }
}
</script>

<style scoped>
</style>