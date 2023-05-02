package com.itachallenge.challenge.compilers;

//See link, there many others.:
// https://rapidapi.com/search/online%2Bcompiler
//Also google "api code compiler"
public enum ApiTarget {

    //to forget. Needs id + key. When singup they don't provide it. email or username + password don't work
    //JDOODLE("https://docs.jdoodle.com/integrating-compiler-ide-to-your-application/compiler-api/rest-api"),

    //meh... not tried
    //CODE_X("https://rapidapi.com/jaagravseal03/api/codex7/details"),
    //5000/day + 0,05$ each extra + needs credit card for free singup + no php neither javascript/node

    //works correctly -> TODO: Help needed for last test in node js
    ONLINE_CODE_COMPILER("https://rapidapi.com/Glavier/api/online-code-compiler/");
    //500/day + free singup + javascrip -> node.js

    //TODO: add more

    private String docsWeb; //api docs

    ApiTarget(String docsWeb) {
        this.docsWeb = docsWeb;
    }

    public String getDocsWeb() { //if needed...
        return docsWeb;
    }
}
