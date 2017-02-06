# Mah
Mah is a alternative to alfred,written in java.

# Features
## Asynchronous
For example,it will take long time to synchronize github repositories you starred;
you can execute other actions like translation while synchronizing.
## Configurable
Configuration is the one of important features,you can configure keybind,plugin,and so on.To configure keybind,you should know what is Mode.Mode is a collection of keybinds,allowing same keybind to execute different actions.For instance,TranslationCommand will trigger translation_mode,within which CopyWord and CopyExplains actions are defined;then you can press alt+w (i defined,you can configure it freely in conf.xml) to execute CopyWord
 action;as a result,translated word will be copied.
## Plugin support
Mah is totally based on plugin.Thinks to java ecosystem,you can develop a plugin with powerful abilities.

# Getting Started
## Installation
Mah doesn't provide any binary package for user as there are some efforts to do this.So to use
Mah,you must compile it yourself;it is not a big deal,only tough thing is to install jdk8.Once installed,the rest is easy.

    git clone https://github.com/zgqq/mah && cd mah 
    ./install

## Configuration
Open /home/{username}/.config/mah/conf.xml with editor,you will see
   
     <?xml version="1.0" encoding="UTF-8"?>
      <config>
       <global>
           <globalKeybind listen="M-space" consume="M-space" action="FocusWindow" />
       </global>
       <mode name="system_mode">
           <keybind bind="C-x C-c" action="ExitSystem" />
       </mode>
       <mode name="window_mode">
           <keybind bind="C-g" action="HideWindow" />
           <keybind bind="M-h" action="MoveWindowToLeft" />
           <keybind bind="M-l" action="MoveWindowToRight" />
           <keybind bind="M-c" action="MoveWindowToCenter" />
       </mode>
       <mode name="input_mode">
           <keybind bind="C-r" action="Redo" />
           <keybind bind="C-/" action="Undo" />
           <!-- line -->
           <keybind bind="C-a" action="BeginningOfLine" />
           <keybind bind="C-e" action="EndOfLine" />
           <keybind bind="C-u" action="KillWholeLine" />
           <keybind bind="C-k" action="KillLine" />
           <!-- char -->
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
       <mode name="item_mode">
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

Above is most basic configuration,mah will generate it automatically,you can modify it freely.

### Translation plugin
First,you need to apply api pair http://fanyi.youdao.com/openapi?path=data-mode

    <plugs>
            <name>translation</name>
    </plugs>
    <plug name="translation">
        <command name="Translation">
            <trigger key="fy" />
            <config>
                <keyfrom>{keyfrom}</keyfrom>
                <apikey>{apkey}</apikey>
            </config>
        </command>
    </plug>


![translation plugin](./screenshot/translation.gif "translation")

### Weather plugin
    <plugs>
            <name>weather</name>
    </plugs>
    <plug name="weather">
        <command name="QueryWeather">
            <trigger key="we" />
            <config>
                <defaultCity>beijing</defaultCity>
            </config>
        </command>
    </plug>

![weather plugin](./screenshot/weather.gif "weather")

### github plugin
First, go to https://github.com/settings/tokens to generate a token

    <plugs>
            <name>github</name>
    </plugs>
    <plug name="github">
        <mode name="github_mode" parent="item_mode">
            <keybind bind="M-i" action="GoGithubIssues" />
            <keybind bind="M-c" action="ClearCache" />
        </mode>
        <command name="GithubStarredList">
            <trigger key="gs" />
            <config>
                <username>{your github usename}</username>
                <!-- 2 minutes -->
                <updateRate>2</updateRate>
                <token>{your token}</token>
                <postCommand></postCommand>
            </config>
        </command>
    </plug>

![github plugin](./screenshot/github.gif "github")

### More plugins
Java libraries are so powerful that it is easy to develop a plugin.

