# mah
* mah 是一个类似于alfred的软件，纯java编写，
java写界面真的没啥问题，何况是这种小项目，intellij idea 就是java写的，在我的机器上，也没有感觉到卡，相当好用。
* 主要针对linux 开发，可能在windows 和macOS 能够正确运行，但是我没有测试过。
Windows和macOS已经有很好的同类软件了，而且功能相当强大，我发现linux下这类软件真的找不到好用的，可能只是我觉得不好用。有时候会看点洋文，有很多单词不认识，虽然可以在终端上查，还是不方便，想想以前都是在网页上查单词，那个效率连我自已都怕，后来看到alfred，发现这种模式非常高效，所以就想写一个来查单词，再后来发现需要查天气，就决定把软件改成类似alfred，所以算是刚好自已有需要，所以写了来提高下生产力 

# 功能
* 这个软件是针对程序猿优化的
* 用linux基本都是键盘流，所以软件不支持鼠标
* 这里解决了键盘流的几个痛点
    1. 每个程序猿多多少少用都会点看洋文，即使你洋文8级应该也有不会的单词吧，浏览器拓展通        常会有取词的插件，都是对于我这种键盘侠来说几乎是没有什么卵用，你手移动到鼠标那里的       时间，我都可以唤起软件查询好了，还有唤起软件的时候有可能遮住那个单词，你能配             置快捷键移动软件的窗口
    2. Github 老司机经常star别人的仓库，然后有空的时候会去看，仓库名一般都不会记得那么清        楚，你最快也要在https://github.com/{username}?page=1&tab=stars 搜索那个仓库吧，然        后点击进入吧，你能确保一次搜索正确？Github插件 同步仓库+模糊搜索解决痛点
* 终端的功能已经很强大了，也是有些不足，所以主要这个软件主要用来做终端的补充，这个软件很傻，不会猜你的想法，很多同类软件都会根据你的输入提示智能提示一些命令什么的，其实这样容易养成不好的习惯，想想精准输入和软件猜你的想法再让你选那个快？
* 软件使用文件来储存配置，这样的一样好处就是有时候重装系统把文件复制过去就可以了，而不是在那里重新点来点去
* 现在模糊搜索是我自已写的，写得不好数据大的时候很慢，很多地方还可以优化 ，但是匹配效果够用，很小数据速度还是可以接受，后期有时间，增加并行搜索有没有改善
* 所有的命令都是异步执行的，这样你可以同时执行多条命令，比如，我执行命令同步github 已经star的仓库的时候，因为同步的过程可能有点久，有点蛋疼，你可以输入其他命令做其他事，查查单词什么的，不会受影响，插件只能通过java编写，具体编写方法，我实在没有精力写什么教程之类的东西，你可以参考 [这几个插件](./mah-plugin) 的写法，但是插件机制感觉还不是很成熟，暂时不建议自已写插件

# 如何使用？
## 启动软件
现在安装比较麻烦，后期如果多人用的，直接做成包，用包管理器安装就很方便了
首先要配置好jdk1.8的环境，
下载[最新版本](../../releases)，然后解压进入目录，执行命令java -jar mah.jar 软件也就启动了，
因为软件没有配置，先杀死进程退出，配置后再启动

## 一步一步配置
用编辑器打开 /home/用户名/.config/mah/conf.xml 加入

    <?xml version="1.0" encoding="UTF-8"?>
    <config>
        <!-- 全局快捷键只能监听按键，并不能消耗那个按键的作用，比如你在配置文件里面配置Ctrl+i为弹出软件，但是一个键击同样可以被其他软件接受到,
        所以你必须让桌面环境消耗掉那个按键，例如窗口管理器i3 加入bindsym Mod1+space exec '' 
        这样Alt+space就被桌面环境消耗了，其他软件就接受不到这个键击了，也就不会做其他动作了。 -->
        <global>
            <!-- s 通常是win 键，space是空格键，这行配置的意思，win+space 唤起软件 -->
            <globalKeybind bind="s-space" action="FocusWindow" />
        </global>
        <mode name="system_mode">
            <!-- system -->
            <!-- C是 control 退出软件  -->
            <keybind bind="C-x C-c" action="ExitSystem" />
        </mode>
        <!-- M通常表示Alt键 -->
        <mode name="window_mode" parent="system_mode">
            <!-- 隐藏软件 -->
            <keybind bind="C-g" action="HideWindow" />
            <!-- 把软件移动到左边 -->
            <keybind bind="M-h" action="MoveWindowToLeft" />
            <!-- 把软件移动到右边 -->
            <keybind bind="M-l" action="MoveWindowToRight" />
            <!-- 把软件移动中间 -->
            <keybind bind="M-c" action="MoveWindowToCenter" />
        </mode>
        <!-- C 表示 Control键 -->
        <mode name="input_mode" parent="window_mode">
            <keybind bind="C-r" action="Redo" />
            <keybind bind="C-/" action="Undo" />
            <!-- line -->
            <keybind bind="C-a" action="BeginningOfLine" />
            <keybind bind="C-e" action="EndOfLine" />
            <!-- 清空输入框 -->
            <keybind bind="C-u" action="KillWholeLine" />
            <keybind bind="C-k" action="KillLine" />
            <!-- char -->
            <!-- 删除字符 -->
            <keybind bind="backspace" action="BackwardDeleteChar" />
            <keybind bind="C-h" action="BackwardDeleteChar" />
            <keybind bind="C-d" action="DeleteChar" />
            <keybind bind="C-f" action="ForwardChar" />
            <keybind bind="C-b" action="BackwardChar" />
            <!-- word -->
            <keybind bind="C-backspace" action="BackwardKillWord" />
            <keybind bind="M-f" action="ForwardWord" />
            <keybind bind="M-b" action="BackwardWord" />
            <keybind bind="M-d" action="KillWord" />
        </mode>
        <mode name="item_mode" parent="input_mode">
                <!-- item -->
                <keybind bind="C-n" action="NextItem" />
                <keybind bind="C-p" action="PreviousItem" />
                <keybind bind="Enter" action="DefaultSelectItem" />
                <keybind bind="M-1" action="SelectItem1" />
                <keybind bind="M-2" action="SelectItem2" />
                <keybind bind="M-3" action="SelectItem3" />
                <keybind bind="M-4" action="SelectItem4" />
                <keybind bind="M-5" action="SelectItem5" />
                <keybind bind="M-6" action="SelectItem6" />
                <keybind bind="M-7" action="SelectItem7" />
                <keybind bind="M-8" action="SelectItem8" />
                <keybind bind="M-9" action="SelectItem9" />
        </mode>
        <theme>dark</theme>
    </config>
    
上面就是最基本的配置，可根据你的喜好自由修改

### 翻译插件
首先你要去申请一个api http://fanyi.youdao.com/openapi?path=data-mode
拿到keyfrom 和apikey后
在配置文件config节点下 加入

    <plugs>
            <!-- 开启翻译插件 -->
            <name>translation</name>
    </plugs>
    <plug name="translation">
        <command name="Translation">
            <trigger key="fy" />
            <config>
                <keyfrom>你申请的keyfrom</keyfrom>
                <apikey>你申请的apikey</apikey>
            </config>
        </command>
    </plug>


![translation plugin](./screenshot/translation.gif "translation")

### 天气插件
    <plugs>
            <!-- 开启天气插件 -->
            <name>weather</name>
    </plugs>
    <plug name="weather">
        <command name="QueryWeather">
            <trigger key="we" />
            <config>
                <defaultCity>北京</defaultCity>
            </config>
        </command>
    </plug>

![weather plugin](./screenshot/weather.gif "weather")

### github插件
这个插件对我太有用了，就是用来搜索已经star的仓库。本插件支持离线搜索和模糊匹配，github老司机必备高速坐骑。
首先你要到 https://github.com/settings/tokens 这里申请一个token 

    <plugs>
            <!-- 开启Github插件 -->
            <name>github</name>
    </plugs>
    <plug name="github">
        <!-- 配置github mode -->
        <mode name="github_mode" parent="item_mode">
            <!-- item -->
            <!-- 打开当前仓库的issues -->
            <keybind bind="M-i" action="GoGithubIssues" />
            <!-- 清零缓存，同步repository -->
            <keybind bind="M-c" action="ClearCache" />
        </mode>
        <command name="GithubStarredList">
            <trigger key="gs" />
            <config>
                <username>你的github用户名</username>
                <!-- 2 minutes 同步速度 -->
                <updateRate>2</updateRate>
                <token>申请到的token</token>
                <!-- 打开浏览器后，执行命令 -->
                <postCommand>i3-msg workspace 2</postCommand>
            </config>
        </command>
    </plug>

![weather plugin](./screenshot/github.gif "github")

### 更多插件
java的类库那么强大，开发一个插件真的easy，比如计算器插件，找个算术解析器的库，就能写出强大的插件了。

## 参考配置

    <?xml version="1.0" encoding="UTF-8"?>
    <config>
        <!-- 全局快捷键只能监听按键，并不能消耗那个按键的作用，比如你在配置文件里面配置Ctrl+i为弹出软件，但是一个键击同样可以被其他软件接受到,
        所以你必须让桌面环境消耗掉那个按键，例如窗口管理器i3 加入bindsym Mod1+space exec '' 
        这样Alt+space就被桌面环境消耗了，其他软件就接受不到这个键击了，也就不会做其他动作了。 -->
        <global>
            <!-- s 通常是win 键，space是空格键，这行配置的意思，win+space 唤起软件 -->
            <globalKeybind bind="s-space" action="FocusWindow" />
        </global>

        <mode name="system_mode">
            <!-- system -->
            <!-- C是 control 退出软件  -->
            <keybind bind="C-x C-c" action="ExitSystem" />
        </mode>
        <!-- M通常表示Alt键 -->
        <mode name="window_mode" parent="system_mode">
            <!-- 隐藏软件 -->
            <keybind bind="C-g" action="HideWindow" />
            <!-- 把软件移动到左边 -->
            <keybind bind="M-h" action="MoveWindowToLeft" />
            <!-- 把软件移动到右边 -->
            <keybind bind="M-l" action="MoveWindowToRight" />
            <!-- 把软件移动中间 -->
            <keybind bind="M-c" action="MoveWindowToCenter" />
        </mode>
        <!-- C 表示 Control键 -->
        <mode name="input_mode" parent="window_mode">
            <keybind bind="C-r" action="Redo" />
            <keybind bind="C-/" action="Undo" />
            <!-- line -->
            <keybind bind="C-a" action="BeginningOfLine" />
            <keybind bind="C-e" action="EndOfLine" />
            <!-- 清空输入框 -->
            <keybind bind="C-u" action="KillWholeLine" />
            <keybind bind="C-k" action="KillLine" />
            <!-- char -->
            <!-- 删除字符 -->
            <keybind bind="backspace" action="BackwardDeleteChar" />
            <keybind bind="C-h" action="BackwardDeleteChar" />
            <keybind bind="C-d" action="DeleteChar" />
            <keybind bind="C-f" action="ForwardChar" />
            <keybind bind="C-b" action="BackwardChar" />
            <!-- word -->
            <keybind bind="C-backspace" action="BackwardKillWord" />
            <keybind bind="M-f" action="ForwardWord" />
            <keybind bind="M-b" action="BackwardWord" />
            <keybind bind="M-d" action="KillWord" />
        </mode>
        <mode name="item_mode" parent="input_mode">
                <!-- item -->
                <keybind bind="C-n" action="NextItem" />
                <keybind bind="C-p" action="PreviousItem" />
                <keybind bind="Enter" action="DefaultSelectItem" />
                <keybind bind="M-1" action="SelectItem1" />
                <keybind bind="M-2" action="SelectItem2" />
                <keybind bind="M-3" action="SelectItem3" />
                <keybind bind="M-4" action="SelectItem4" />
                <keybind bind="M-5" action="SelectItem5" />
                <keybind bind="M-6" action="SelectItem6" />
                <keybind bind="M-7" action="SelectItem7" />
                <keybind bind="M-8" action="SelectItem8" />
                <keybind bind="M-9" action="SelectItem9" />
        </mode>

        <theme>dark</theme>

        <plugs>
            <name>translation</name>
            <!-- 开启天气插件 -->
            <name>weather</name>
            <!-- 开启Github插件 -->
            <name>github</name>
        </plugs>
        <plug name="translation">
                <command name="Translation">
                    <trigger key="fy" />
                    <config>
                        <keyfrom>你申请的keyfrom</keyfrom>
                        <apikey>你申请的apikey</apikey>
                    </config>
                </command>
            </plug>
        <plug name="weather">
                <command name="QueryWeather">
                    <trigger key="we" />
                    <config>
                        <defaultCity>北京</defaultCity>
                    </config>
                </command>
        </plug>
        <plug name="github">
                <!-- 配置github mode -->
                <mode name="github_mode" parent="item_mode">
                    <!-- item -->
                    <!-- 打开当前仓库的issues -->
                    <keybind bind="M-i" action="GoGithubIssues" />
                    <!-- 清零缓存，同步repository -->
                    <keybind bind="M-c" action="ClearCache" />
                </mode>
                <command name="GithubStarredList">
                       <trigger key="gs" />
                       <config>
                            <username>你的github用户名</username>
                            <!-- 2 minutes 同步速度 -->
                            <updateRate>2</updateRate>
                            <token>申请到的token</token>
                            <!-- 打开浏览器后，执行命令 -->
                            <postCommand>i3-msg workspace 2</postCommand>
                       </config>
               </command>
        </plug>
    </config>


# 欢迎Pull Request
现在mah已经基本满足我的要求，有空的大神们可以帮忙完善一下，请各位看到这里的大佬给个star吧
