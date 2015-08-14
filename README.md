# minifyresources
A example project to show how to use minify plugin with maven



As we develop our web applications, we will see a lot of static resource in our web pages, jquery, bootstrap's, javascript, css and others libraries that we use, this will create a lot of request to our web pages be loaded, and we know that is not a good practice and make our web page to load a bit slowly. We can reduce this time using a maven plugin when building our Java project, Minify Maven Plugin, it will group and minify ( if we want to )  our static resources, so we can reduce the trips to the server. 


To use, is very simple, with you maven project you will specify the plugin in the build section

```xml
...
<plugin>
	<groupId>com.samaxes.maven</groupId>
	<artifactId>minify-maven-plugin</artifactId>
	<version>1.7.4</version>
	<executions>
		<execution>
			<id>default-minify</id>
			<phase>package</phase>
			<configuration>
				<webappSourceDir>${basedir}/src/main/webapp/resources</webappSourceDir>
				<cssSourceDir>css</cssSourceDir>
				<cssSourceFiles>
					<cssSourceFile>style.css</cssSourceFile>
					<cssSourceFile>other-style.css</cssSourceFile>
					<cssSourceFile>ateste.css</cssSourceFile>
				</cssSourceFiles>
				<cssTargetDir>resources/css</cssTargetDir>
				<cssFinalFile>all.css</cssFinalFile>
				<jsSourceFiles>
  				<jsSourceFile>framework.js</jsSourceFile>
					<jsSourceFile>ordered.js</jsSourceFile>
				</jsSourceFiles>
				<jsTargetDir>resources/js</jsTargetDir>
				<jsFinalFile>all.js</jsFinalFile>
			</configuration>
			<goals>
				<goal>minify</goal>
			</goals>
		</execution>
	</executions>
</plugin>
...
```

Let's rapidly explain what is going on here, the tag <webappSourceDir> will define where is my source directory, as you can see i’m pointing to the “webapp/resources” directory, then we have the <cssSourceDir> tag, there we specify where is the directory of the css, inside the webappSourceDir previously especified, the same goes for the javascript configurations, where the only diference is the js prefix.

With that, when i’m building my project, the plugin will group the resources and minify it, but it will only generate the minified files, i still have to change my application to point to my new resource file, to do that, we will use another plugin called replacer, from google, note that the use of the minified files is interesting only in production environment, so we will create a maven profile for this environment

```xml
<profiles>
		<profile>
			<id>DEV</id>
			<activation>
				<activeByDefault>true</activeByDefault>
			</activation>
		</profile>
		<profile>
			<id>PROD</id>
			<build>
			<plugins>
				<plugin>
					<groupId>com.google.code.maven-replacer-plugin</groupId>
					<artifactId>replacer</artifactId>
					<version>1.5.0</version>
					<executions>
						<execution>
							<phase>prepare-package</phase>
							<goals>
								<goal>replace</goal>
							</goals>
						</execution>
					</executions>
					<configuration>
						<file>target/${project.build.finalName}/WEB-INF/template/_head.jsp</file>
						<outputFile>target/${project.build.finalName}/WEB-INF/template/_head.jsp</outputFile>
						<replacements>
							<replacement>
								<token>
										(?m)&lt;!--replaceCSS(.|\s)*?replaceCSSEnd --&gt; </token>
								<value>&lt;link rel="stylesheet" type="text/css" href="&lt;c:url value='/resources/css/all-${maven.build.timestamp}.min.css'/&gt;"/&gt;</value>
							</replacement>
							<replacement>
								<token>(?m)&lt;!--replaceJS(.|\s)*?replaceJSEnd --&gt;</token>
								<value>&lt;script type="text/javascript" src="&lt;c:url value='/resources/js/all-${maven.build.timestamp}.min.js'/&gt;"&gt;&lt;/script&gt;</value>
							</replacement>
						</replacements>
					</configuration>
				</plugin>
			</plugins>
		</build>
	</profile>
</profiles>
```

Note that we will have two profiles, one for development, active by default, and another for production, this one, will have the plugin configuration for the replacement of the minified references. Note that the exectuion phase will be “prepare-package” , so it will be executed before packing my .war

In the configuration section in plugin tag, we have the file tag, where i will point the file that i want to process, and the outputFile tag, that points to the destination of the processed file, in this case we are pointing for the same file 

The replacements tag, points what i will replace, in this case we use a regex, see in <token>,
points that we will replace anything between the two comments replaceCSS and replaceCSSEnd, and in the value tag, what the value that will be placed


This is my _head.jsp file in development environment
```html
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set value="${pageContext.request.contextPath}" var="path" ></c:set>

<!--replaceCSS-->
<link rel="stylesheet" type="text/css" href="${path}/resources/css/style.css" />
<link rel="stylesheet" type="text/css" href="${path}/resources/css/other-style.css" />
<link rel="stylesheet" type="text/css" href="${path}/resources/css/ateste.css" />
<!--replaceCSSEnd -->	

 <!--replaceJS-->
<script type="text/javascript" src="${path}/resources/js/framework.js"></script>
<script type="text/javascript" src="${path}/resources/js/ordered.js"></script>
 <!--replaceJSEnd -->
``` 
and this is what i spect to get in my production environment

This is my _head.jsp file in development environment
```html
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/all-20150812001451.min.css'/>"/>	

<script type="text/javascript" src="<c:url value='/resources/js/all-20150812001451.min.js'/>"></script>
```
With that, we don't interfere with the development process and still get a good production code!

To build to production profile: mvn clean package -P PROD
To build to development profile: mvn clean package 
