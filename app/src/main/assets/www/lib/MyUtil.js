/**
 * Created by Administrator on 2016/8/30.
 */
/*
1、对于string,number等基础类型，==和===是有区别的
1）不同类型间比较，==之比较“转化成同一类型后的值”看“值”是否相等，===如果类型不同，其结果就是不等
2）同类型比较，直接进行“值”比较，两者结果一样
2、对于Array,Object等高级类型，==和===是没有区别的
进行“指针地址”比较
3、基础类型与高级类型，==和===是有区别的
1）对于==，将高级转化为基础类型，进行“值”比较
2）因为类型不同，===结果为false*/

//首先要对HTMLElement进行类型检查，因为即使在支持HTMLElement
//的浏览器中，类型却是有差别的，在Chrome,Opera中HTMLElement的
//类型为function，此时就不能用它来判断了
var isDOM = ( typeof HTMLElement === 'object' ) ?
    function(obj){
        return obj instanceof HTMLElement;
    } :
function(obj){
    return obj && typeof obj === 'object' && obj.nodeType === 1 && typeof obj.nodeName === 'string';
};
