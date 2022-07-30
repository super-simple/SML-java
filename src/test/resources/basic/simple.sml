sml() //保留,用于以后扩展
document(foo="bar" bar="foo"){
    //body 只包含节点
    body{
        //h1 只包含值
        h1{hello document}
        h2{hello document}
    }
    ul[
        li{aa{123}}
        li{aa{123}}
        li{aa{123}}
        li{aa{123}}
    ]
    ul[
    123
    123
    213
    123
    123
    ]
    h1{`h1{}`}//相当于CDATA
}