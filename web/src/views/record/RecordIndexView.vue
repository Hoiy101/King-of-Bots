<template>
    <ContentField>
                   <div class="card-body">
                        <table class="table table-striped table-hover" style="text-align: center;">
                           <thead>
                                <tr>
                                    <th>A玩家</th>
                                    <th>B玩家</th>
                                    <th>对局结果</th>
                                    <th>对战时间</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="record in records" :key="record.record.id">
                                    <td>
                                        <img :src="record.a_photo" alt="" class="record-user-photo">
                                        &nbsp;
                                        <span class="record-user-username"> {{ record.a_username }}</span>
                                    </td>
                                    <td>
                                        <img :src="record.b_photo" alt="" class="record-user-photo">
                                        &nbsp;
                                        <span class="record-user-username"> {{ record.b_username }}</span>
                                    </td>
                                    <td>{{ record.result }}</td>
                                    <td>{{ record.record.createtime }}</td>
                                    <td>
                                        <button @click="open_record_content(record.record.id)" class="btn btn-sm btn btn-primary btn">查看录像</button>

                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                        <nav aria-label="...">
                        <ul class="pagination" style = "float: right;">
                            <li class="page-item">
                                <a href="#" class="page-link" @click="click_page(-1)">上一页</a>
                            </li>
                            <li class="page-item" v-for="page in pages" :key = "page.number">
                                <a :class="'page-link ' + page.is_active" @click="click_page(page.number)" href="#">{{ page.number }}</a>
                            </li>
                            <li class="page-item">
                                <a class="page-link" href="#" @click="click_page(-2)">下一页</a>
                            </li>
                        </ul>
                        </nav>   
    </ContentField>
    
</template>

<script>
import ContentField from '@/components/ContentField.vue';
import { useStore } from 'vuex';
import { ref } from 'vue';
import $ from 'jquery';
import router from '@/router/index.js';

export default{
    components: {
        ContentField
    },
    setup(){
        const store = useStore();
        let records = ref([]);
        let current_page = 1;
        let total_records = 0;
        let pages = ref([]);

        const click_page = page => {
            if(page === -1) {
                page = current_page - 1;
            }
            else if(page === -2) {
                page = current_page + 1;
            }
            else{
                page = parseInt(page);
            }
            if(page < 1 || page > parseInt(Math.ceil(total_records / 10))) return;
            pull_page(page);

        }

        const update_pages = () => {
            let max_pages = parseInt(Math.ceil(total_records / 10));
            let new_pages = [];
            for(let i = current_page - 2; i <= current_page + 2; i ++){
                if(i >= 1 && i <= max_pages){
                new_pages.push({
                    number: i,
                    is_active: i === current_page ? "active" : "",
                });
                }
            }
            pages.value = new_pages;
        }

        const pull_page = page => {
            current_page = page;
            $.ajax({
                url : "http://127.0.0.1:3000/record/getlist/",
                type : "get",
                headers: {
                    Authorization: "Bearer " + store.state.user.token,
                },
                data: {
                    page,
                },
                success(resp){
                    console.log(resp);
                    records.value = resp.items;
                    total_records = resp.records_count;
                    update_pages();
                },
                error(resp){
                    console.log(resp);
                }
            })
        }
        pull_page(current_page);

        const stringTo2D = map => {
            let g = [];
            for(let i = 0, k = 0; i < 13; i ++){
                let line = [];
                for(let j = 0; j < 14; j ++, k ++){
                    if(map[k] === '0') line.push(0);
                    else line.push(1);
                }
                g.push(line);
            } 
            return g;
        }

        const open_record_content = recordId => {
            for(const record of records.value){
                if(record.record.id === recordId){
                    store.commit("updateIsRecord", true);
                    console.log(record);
                    store.commit("updateGame", {
                        map: stringTo2D(record.record.map),
                        a_id: record.record.aid,
                        b_id: record.record.bid,
                        a_sx: record.record.asx,
                        a_sy: record.record.asy,
                        b_sx: record.record.bsx,
                        b_sy: record.record.bsy,

                    });
                    store.commit("updateSteps", {
                        a_steps: record.record.asteps,
                        b_steps: record.record.bsteps,
                    })
                    store.commit("updateRecordLoser", record.record.loser);
                    router.push({ name: "record_content", params: { recordId } });
                    break;
                }
            }
        }

        return {
            records,
            total_records,
            open_record_content,
            pages,
            click_page,
            current_page,
        }
    }
    
}
</script>

<style scoped>
img.record-user-photo {
    width:4vh;
    border-radius: 50%;
}
</style>