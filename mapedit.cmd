@echo off
SET CLASSPATH=target/*;lib/*;
java -Xms256m -Xmx1024m -Xss128k -Djava.library.path=. com.github.yjcpaj4.play_with_us.tool.MapEditTool
@pause