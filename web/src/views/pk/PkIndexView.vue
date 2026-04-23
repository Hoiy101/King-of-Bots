<template>
     <PlayGround v-if="$store.state.pk.status === 'playing'"/>
     <MatchGround v-if="$store.state.pk.status === 'matching'"/>
     <ResultBoard v-if="$store.state.pk.loser != 'none'"/>
</template>

<script>
import PlayGround from '@/components/PlayGround.vue';
import MatchGround from '@/components/MatchGround.vue';
import ResultBoard from '@/components/ResultBoard.vue';
import { onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';

export default{
    components: {
        PlayGround,
        MatchGround,
        ResultBoard,
    },
    setup(){
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;

        store.commit("updateIsRecord", false);

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
                    store.commit("updateGame", data.game);
                    setTimeout(() =>{
                        store.commit("updatestatus", "playing");  
                    }, 200);              
                }else if(data.event === "move"){
                    console.log(data);
                    const game = store.state.pk.GameObject;
                    const [snake0, snake1] = game.snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);

                }else if(data.event === "result"){
                    console.log(data);
                    const game = store.state.pk.GameObject;
                    const [snake0, snake1] = game.snakes;
                    store.commit("updateloser", data.loser);
                    if(data.loser === "all" || data.loser === "a"){
                        snake0.status = "die";
                    }
                    if(data.loser === "all" || data.loser === "b"){
                        snake1.status = "die"; 
                    }
            }
            socket.onclose = () => {
                console.log("WebSocket连接已关闭");
                store.commit("updatestatus", "matching"); 
                store.commit("updateloser", "none");
            }
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