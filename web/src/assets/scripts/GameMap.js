import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";
import { Snake } from './Snake';
export class GameMap extends AcGameObject{
    constructor(ctx,parent, store){
        super();

        this.ctx = ctx;
        this.parent = parent;
        this.store = store;
        this.L = 0;
 
        this.rows = 13;
        this.cols = 14;

        this.inner_walls_count = 20;
        this.walls = [];

        this.snakes = [
            new Snake({id: 0, color: "#4876EC", r: this.rows - 2, c: 1}, this),
            new Snake({id: 1, color: "#F94848", r: 1, c: this.cols - 2}, this),
        ];

    }
    
    create_walls(){ // 创建障碍物
        const g = this.store.state.pk.gamemap;
        
        for(let r = 0; r < this.rows; r ++){
            for(let c = 0; c < this.cols; c ++){
                if(g[r][c]){
                    this.walls.push(new Wall(r, c, this));
                }
            }
        }
        return true;
    }

    add_listening_events(){
        this.ctx.canvas.focus();
        this.ctx.canvas.addEventListener("keydown", e => { // 从键盘中读入方向，并且不会回头
            let d = -1;
            if(e.key === 'w' || e.key === 'W') d = 0;
            else if(e.key === 'd' || e.key === 'D') d = 1;
            else if(e.key === 's' || e.key === 'S') d = 2;
            else if(e.key === 'a' || e.key === 'A') d = 3;
 
            if(d >= 0){
                this.store.state.pk.socket.send(JSON.stringify({
                    event: "move",
                    direction: d,
                }));
            }
        });
    }

    start(){
        this.create_walls();
        this.add_listening_events();
    }
    update_size(){ //算出地图的相对长宽
        this.L = Math.min(Math.trunc(this.parent.clientWidth/this.cols), Math.trunc(this.parent.clientHeight/this.rows));
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    next_step(){
        for(const snake of this.snakes){
            snake.next_step();
        }
    }

    check_ready(){
        for(const snake of this.snakes){
            if(snake.status !== "idle") return false;
            if(snake.direction === -1) return false;
        }
        return true;
    }

    check_valid(cell){
        for(const wall of this.walls){
            if(wall.r === cell.r && wall.c === cell.c){
                return false;
            }
        }

        for(const snake of this.snakes){
            let k = snake.cells.length;
            if(!snake.check_tall_increasing()){
                k --;
            }
            for(let i = 0; i < k; i ++){
                if(snake.cells[i].r === cell.r && snake.cells[i].c === cell.c){
                    return false;
                }
            }
        }
        return true;
    }

    update(){
        this.update_size();
        if(this.check_ready()){
            this.next_step();
        }
        this.render();
    }

    render(){ //创建地图
        const color_even = "#AAD751", color_odd = "#8eca18ff"; 
          for(let r = 0; r < this.rows; r ++){
            for(let c = 0; c < this.cols; c ++){
                if((r + c) % 2 == 0){
                    this.ctx.fillStyle = color_even;
                }
                else{
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(c * this.L, r * this.L, this.L, this.L);
            }
          }
    }
}