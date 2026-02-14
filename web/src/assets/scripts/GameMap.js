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

    check_connectivity(g, sx, sy, tx, ty){ // 判断左下角和右上角是否连通
        if(sx == tx && sy == ty) return true;
        g[sx][sy] = true;
        
        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];
        for(let i = 0; i < 4; i ++){
            let x = sx + dx[i], y = sy + dy[i];
            if(!g[x][y] && this.check_connectivity(g, x, y, tx, ty)){
                return true;
            }
        }
        return false;
    }

    create_walls(){ // 创建障碍物
        const g = this.store.state.pk.Gamemap;
        
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

        const [snake0, snake1] = this.snakes;
        this.ctx.canvas.addEventListener("keydown", e => { // 从键盘中读入方向，并且不会回头
            if(e.key === 'w') snake0.set_direction(0);
            else if(e.key === 'd') snake0.set_direction(1);
            else if(e.key === 's') snake0.set_direction(2);
            else if(e.key === 'a') snake0.set_direction(3);
            else if(e.key === 'ArrowUp')snake1.set_direction(0);
            else if(e.key == 'ArrowRight')snake1.set_direction(1);
            else if(e.key == 'ArrowDown')snake1.set_direction(2);
            else if(e.key == 'ArrowLeft')snake1.set_direction(3);
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