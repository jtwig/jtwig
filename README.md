# JTwig 1.0 Beta

*JTwig is a template mechanism, inspired in [Twig](http://twig.sensiolabs.org/) (PHP), for [SpringFramework Web MVC](http://www.springsource.org) (Java).*

### Why beta?

It is currently in beta version stage - we are testing it under heavy scenarios, in real world cases. It will also be enhanced by adding quite a few functions.


### Architecture

JTwig architecture is really simple. It consists of a [Parboiled Parser](https://github.com/sirthias/parboiled), that processes the template. After assembling all Extends and Includes macros, it renders the output.

### Example

File: layout.twig
<pre><code>&lt;html&gt;
	&lt;head&gt;
		&lt;title&gt;{% block title %}Unknown{% endblock %}&lt;/title&gt;
	&lt;/head&gt;
	&lt;body&gt;
		&lt;h1&gt;This is a test&lt;/h1&gt;
		{% block content %}
		No content defined
		{% endblock %}
	&lt;/body&gt;
&lt;/html&gt;
</code></pre>

File: hello.twig
<pre><code>{% extends 'layout.twig' %}
{% block title %}Hello World Page{% endblock %}
{% block content %}
&lt;p&gt;Hello World&lt;/p&gt;
{% endblock %}
</code></pre>

The wiki contains more [documentation](/lyncode/jtwig/wiki).

----------

Maven
-----

XOAI could be integrated with maven support.

	<dependency>
	    <groupId>com.lyncode</groupId>
	    <artifactId>xoai</artifactId>
	    <version>3.1.0</version>
	</dependency>

### License


Copyright 2012 **Lyncode**

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License. You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.
