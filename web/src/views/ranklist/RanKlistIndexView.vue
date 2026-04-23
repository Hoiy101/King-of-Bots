<template>
    <ContentField>
        <div class="card-body">
            <table class="table table-striped table-hover" style="text-align: center;">
                <thead>
                    <tr>
                        <th>排名</th>
                        <th>玩家</th>
                        <th>评分</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(user, index) in ranklist" :key="user.id">
                        <td>
                            <span v-if="index === 0" class="rank-badge gold">🥇</span>
                            <span v-else-if="index === 1" class="rank-badge silver">🥈</span>
                            <span v-else-if="index === 2" class="rank-badge bronze">🥉</span>
                            <span v-else>{{ index + 1 }}</span>
                        </td>
                        <td>
                            <img :src="user.photo" alt="" class="rank-user-photo">
                            &nbsp;
                            <span class="rank-user-username">{{ user.username }}</span>
                        </td>
                        <td class="rating">{{ user.rating }}</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </ContentField>
</template>

<script>
import ContentField from '@/components/ContentField.vue';
import { useStore } from 'vuex';
import { ref, onMounted } from 'vue';
import $ from 'jquery';

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        const ranklist = ref([]);

        const pull_ranklist = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/ranklist/getlist/",
                type: "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                success(resp) {
                    ranklist.value = resp;
                },
                error(resp) {
                    console.log(resp);
                }
            })
        }

        onMounted(() => {
            pull_ranklist();
        });

        return {
            ranklist,
        }
    }
}
</script>

<style scoped>
img.rank-user-photo {
    width: 4vh;
    border-radius: 50%;
}

.rank-badge {
    font-size: 1.5em;
}

.rating {
    font-weight: bold;
    color: #007bff;
}
</style>