# EProfiler

## Configuration

Profiler parameters are passed through command line, separated by `;`.  
Parameters that have key and value are separated by `=`. For example  
`-javaagent:eprofiler.jar=classPattern=org.example.*;outputFolder=path/to/logs;debug;showBytecode;minDurationToLog=10`

### List of parameters

* *outputFolder* - **required**, path to the folder, where logs will be stored
* *classPattern* - **required**, only classes that matches specified patterns will be profiled, multiple patterns may be defined 
(e.g. `classPattern=org.example.*;classPattern=org.another.example.*;`)
* *debug* - log debug information, default is *false*
* *showBytecode* - log bytecode of instrumented classes
* *minDurationToLog* - only methods which took longer than specified time (in milliseconds) will be logged. 
If method takes less, it's time is added to it's parent time.

## How to run

### On tomcat

Create the file `PATH_TO_APACHE/bin/setenv.bat` (or `setenv.sh` for Linux) if missing and add 

`CATALINA_OPTS="$CATALINA_OPTS -javaagent:path/to/eprofiler.jar=args"`

### In JUnit

Java Attach API can be used to instrument the classes at runtime. To use it, you must have `tools.jar` on your classpath. 

To add `tools.jar` in maven, add it to your pom:

```
<dependency>
  <groupId>com.sun</groupId>
  <artifactId>tools</artifactId>
  <version>1.7</version>
  <scope>system</scope>
  <systemPath>${java.home}/../lib/tools.jar</systemPath>
</dependency>
```

To use Attach API add this before your tests (for example in static code block)
```
try {
    VirtualMachine vm = VirtualMachine.attach(vmPid);
    vm.loadAgent("path\\to\\eprofiler.jar", "args");
    vm.detach();
} catch (Exception e) {
    throw new RuntimeException(e);
}
```

You can get vm process pid this way
```
String nameOfRunningVM = ManagementFactory.getRuntimeMXBean().getName();
int p = nameOfRunningVM.indexOf('@');
String pid = nameOfRunningVM.substring(0, p);
```
