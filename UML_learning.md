# UML

## 1.类图（Class Diagram）

UML类图中每一个矩形代表一个类

一个类图分为五个部分：

* 1.类名

* 2.属性

* 3.方法

* 4.描述 （描述类的性质

* 5.内部类


类之间关系的表示方式：

1. 泛化（Generalization）
   【泛化关系】：是一种继承关系，表示一般与特殊的关系，它指定了子类如何特化父类的所有特征和行为。例如：老虎是动物的一种，即有老虎的特性也有动物的共性。
   【箭头指向】：带三角箭头的实线，箭头指向父类
   ![img](https://img-blog.csdn.net/20180610203236947?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2lidWtpa29ub2hh/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

2. 实现（Realization）
   【实现关系】：在这里插入图片描述是一种类与接口的关系，表示类是接口所有特征和行为的实现.

   【箭头指向】：带三角箭头的虚线，箭头指向接口

   ![img](https://img-blog.csdn.net/20180610203257351?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2lidWtpa29ub2hh/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

3. 组合(Composition)
   【组合关系】：是整体与部分的关系，但部分不能离开整体而单独存在。如公司和部门是整体和部分的关系，没有公司就不存在部门。
   组合关系是关联关系的一种，是比聚合关系还要强的关系，它要求普通的聚合关系中代表整体的对象负责代表部分的对象的生命周期。
   【代码体现】：成员变量

   【箭头及指向】：带实心菱形的实线，菱形指向整体
   ![img](https://img-blog.csdn.net/20180610204917734?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2lidWtpa29ub2hh/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

4. 聚合（Aggregation）
   【聚合关系】：是整体与部分的关系，且部分可以离开整体而单独存在。如车和轮胎是整体和部分的关系，轮胎离开车仍然可以存在。
   聚合关系是关联关系的一种，是强的关联关系；关联和聚合在语法上无法区分，必须考察具体的逻辑关系。
   【代码体现】：成员变量
   【箭头及指向】：带空心菱形的实心线，菱形指向整体
   ![img](https://img-blog.csdn.net/20180610204525139?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2lidWtpa29ub2hh/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

5. 关联（Association)
    【关联关系】：是一种拥有的关系，它使一个类知道另一个类的属性和方法；如：老师与学生，丈夫与妻子关联可以是双向的，也可以是单向的。双向的关联可以有两个箭头或者没有箭头，单向的关联有一个箭头。

  【代码体现】：成员变量

  【箭头及指向】：带普通箭头的实心线，指向被拥有者

   ![img](https://img-blog.csdn.net/20180610204039677?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2lidWtpa29ub2hh/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

6. 依赖(Dependency)
   【依赖关系】：是一种使用的关系，即一个类的实现需要另一个类的协助，所以要尽量不使用双向的互相依赖.

   【代码表现】：局部变量、方法的参数或者对静态方法的调用

   【箭头及指向】：带箭头的虚线，指向被使用者

   ![img](https://img-blog.csdn.net/20180610203357948?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2lidWtpa29ub2hh/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

   **各种关系的强弱顺序：泛化 = 实现 > 组合 > 聚合 > 关联 > 依赖**

   ![img](https://pic3.zhimg.com/80/v2-d7a10c066ac120affdedcddaab761c76_720w.jpg)
   
## 2.顺序图（Sequence Diagram）

UML顺序图一般用于确认和丰富一个使用情境的逻辑

顺序图将交互关系表现为一个二维图，纵向是时间轴，时间沿竖线向下延伸。横向轴代表了在协作中各独立对象的类元角色，类元角色的活动用生命线表示。、

**基本元素**：

角色(Actor)、对象(Object)、生命线(LifeLine)、控制焦点(Activation)、消息(Message)、自关联消息、组合片段

1.生命线用一条纵向虚线表示

2.在UML中，对象表示为一个矩形

3.控制焦点是过程的执行，包括等待过程执行的时间。在顺序图中激活部分替换生命线，使用长条的矩形表示

4.消息是对象之间的通信，是两个对象之间的单路通信，是从发送者到接收者之间的控制信息流。消息在顺序图中由有标记的箭头表示，箭头从一个对象的生命线指向另一个对象的生命线，消息按时间顺序在图中从上到下排列。

5.在顺序图中，对象安排在X轴。启动交互的对象放在最左边，随后放入消息的对象放在启动交互对象的右边。交互中对象发送和接收的消息沿着Y轴以时间增加的次序放置。

**消息的种类**：

1.调用消息：有的消息对应于激活，表示它将会激活一个对象

2.自身消息：自己发送给自己的消息，对应某种作用于自身的操作，对应自身的两个激活框

3.返回消息：对于调用者的某种回应，发送消息至调用者激活框

4.同步消息：发送消息的对象要等到接收消息的对象执行完所有操作后，发送消息的对象才能继续执行自己的操作。

5.异步消息：发送消息的对象发送消息后，不用等待接收对象是否执行，继续执行自己的操作。

6.创建消息：表示创建新的实例或对象，启动其生命线

7.删除消息：表示删除实例或者对象，结束其生命线

**组合片段**：

用来解决交互执行的条件和方式，它允许在序列图中直接表示逻辑组件，用于通过指定条件或子进程的应用区域，为任何生命线的任何部分定义特殊条件和子进程。组合片段共有13种，名称及含义如下：

![img](https://img-blog.csdn.net/20180704151815882?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2ZseV96eHk=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

## 3.状态图（State Diagram）

状态图(State Diagram)用来描述一个特定对象的所有可能状态及其引起状态转移的事件

* 描述哪些状态之间可以转换

* 不同的状态下有不同的行为

组成元素：

初始状态、终止状态、状态、转移、守护条件、事件、动作

* 状态图用**初始状态（Initial State）**表示对象创建时的状态，每一个状态图一般只有一个初始状态，用实心的圆点表示
* **终止状态（Final State）**，用一个实心圆外加一个圆圈表示
* 状态图中可有多个状态框，每个状态框中有两格：**上格**放置**状态名称**，**下格**说明处于该状态时，系统或对象**要进行的活动（Action）**
* 从一个状态到另一个状态之间的连线称为**转移（Transition）**。状态之间的**过渡事件（Event）**对应对象的动作或活动（Action）。事件有可能在特定的条件下发生，在UML中这样的条件称为**守护条件（Guard Condition）**，发生的**事件**可通过**对象的动作（Action）**进行处理。状态之间的转移可带有标注，由三部分组成（每一部分都可省略），其语法为：**事件名 [条件] / 动作名**

简单状态：不包含其他状态的状态称为简单状态

复合状态：又称为组合状态，可以将若干状态组织在一起可以得到一个复合状态，包含在一个复合状态中的状态称为子状态。

![img](https://img-blog.csdn.net/20161230194757054?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbGl0aWFueGlhbmdfa2FvbGE=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)
