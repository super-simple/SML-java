# ss-java

sml java implementation

[SML-specification](https://github.com/super-simple/SML-specification)

## notice

## 实现

### 数据模式

1. 反序列化,支持属性
2. 序列化,暂时不支持属性

```text
sml() //保留,用于以后扩展
document(foo="bar" bar="foo"){
    //body 只包含节点
    body{
        //h1 只包含值
        h1{
            hello document
        }
        h2{
            hello document
        }
    }
    ul[
        li{123}
        li{123}
        li{123}
        li{123}
    ]
}
```