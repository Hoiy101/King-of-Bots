export default {
    state: {
        status: "matching", // matching表示匹配界面，playing表示对战界面
        socket: null,
        opponent_username: "",
        opponent_photo: "",
        Gamemap: null,
    },
    getters: {
    },
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateOpponent(state, opponent) {
            state.opponent_username = opponent.username;
            state.opponent_photo = opponent.photo;
        },
        updatestatus(state, status) {
            state.status = status; 
        },
        updateGamemap(state, Gamemap) {
            state.Gamemap = Gamemap;
        },
    },
    actions: {
    },
    modules: {
    },
}