# sml-java

sml java implementation

Sml is a variant of xml,the letter s means simple, which provides one more choice.
The syntax of sml imitates C series languages,
which is more friendly, modern, convenient, and completely equivalent to xml

This repository provides java implementation of sml based on sml specification,
only implement data mode and configuration mode,
and does not provide support for document mode

[SML-specification](https://github.com/super-simple/SML-specification)

## implement

## config model

more like xml,no mixed,but sml's style

1. only deserialization

```text
sml() // keep for extend
doucment(foo="bar1" bar=123){ //object have attribute and root element have element name
    h1(123)
    h2("hello")
    div{
        h1(123)
        h2("hello")
    }
    ul[
        li{}
        li{}
        li{}
        li{}
    ]
}
```

### 数据模式

more like json,no mixed,no attribute,but sml's style

1. deserialization
2. Serialization

```text
{
    attributeSimpleNode1("string") //simple attribute with (
    attributeContainerNode1{ //container attribute with {
        attributeSimpleNode2("string")
        attributeSimpleNode3(123)
    }
    /*
        array elements usually have the same element,but it also support different type,depends on your requirement
    */
    attributeContainerNode2[ //container attribute with [,
        123
        "string"
        [123,456] //no attribute name,array element
        null //null element
        true //bool element of true
        false // bool element of false
        { //no attribute name,object element
            attributeSimpleNode4(123)
        }
    ]
}
```