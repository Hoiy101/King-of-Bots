<template>
    <div class="result-board">
        <div class="result-board-text" v-if = "$store.state.pk.loser === 'all'">
            平局!
        </div>
        <div class="result-board-text" v-else-if = "$store.state.pk.loser === 'a' && $store.state.pk.a_id == $store.state.user.id ">
            你输了!
        </div>
        <div class="result-board-text" v-else-if = "$store.state.pk.loser === 'b' && $store.state.pk.b_id == $store.state.user.id ">
            你输了!
        </div>
        <div class="result-board-text" v-else>
            你赢了!
        </div>
        <div class="result-board-btn">
            <button @click="restart" class="btn btn-primary btn-lg">
                再来一局!
            </button>
        </div>
    </div>
</template>

<script>
//import { useStore } from 'vuex';

export default{
    setup(){
        //const store = useStore();

        const restart = () => { 

            window.location.reload();
        }
        return{
            restart,
        }
    }
}
</script>

<style scoped>
div.result-board{
    height: 30vh;
    width: 30vw;
    background-color: rgba(50, 50, 50, 0.5);
    position: absolute;
    top: 30vh;
    left: 35vw;
}
div.result-board-text{
    text-align: center;
    padding-top: 5vh;
    font-size: 40px;
    font-weight: 600;
    font-style: italic;
    color: white;
}
div.result-board-btn{
    text-align: center;
    padding-top: 5vh;
}
</style>