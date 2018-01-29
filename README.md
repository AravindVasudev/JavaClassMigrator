# JavaClassMigrator
JCM is a simple refactoring tool that basically copies specified class members from one class to another. This is useful when refactoring large codebases.

## Dependencies
[javaparser](https://github.com/javaparser/javaparser)

## Compilation
With Java 8 or above and Maven installed, run the following to compile JCM into a single jar:

```
mvn clean compile assembly:single
```

## Usage

```
java -jar jcm.jar [source/class/location.java] [destination/class/location.java] [-p/-m] (member_name[s])
```

The usage syntax is source class location followed by destination class location which is followed by either -p for class property or -m for class method which is followed by a set of member names that has to be moved. If the destination class does not exist, it is generated with the same package as the source class.

### Example

```
java -jar jcm.jar ./Foo.java ./Bar.java -m sayHello sayWorld
```

`sayHello` and `sayBye` methods are copied from Foo class to Bar class.

## Contribute

You are always welcome to open an issue or provide a pull-request!

## License

Built under [MIT](LICENSE) license.