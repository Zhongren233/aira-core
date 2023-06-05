package moe.aira.onebot.plugin;

import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.MessageEvent;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import groovy.util.GroovyScriptEngine;
import moe.aira.onebot.util.AiraBotPlugin;
import moe.aira.onebot.util.AiraSendMessageUtil;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.codehaus.groovy.syntax.Types;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;

@Component
public class ShellPlugin extends AiraBotPlugin {
    public ShellPlugin(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public boolean checkMessage(@NotNull Bot bot, @NotNull MessageEvent event) {
        String trim = event.getMessage().trim();
        return trim.startsWith(">");
    }

    private final ApplicationContext context;

    @Override

    public Runnable doCommand(Bot bot, MessageEvent event) {
        Binding binding = new Binding();
        binding.setVariable("context", context);
        return () -> {
            String trim = event.getMessage().trim();
            String substring = trim.substring(1);
            CompilerConfiguration config = new CompilerConfiguration();
            SecureASTCustomizer secureASTCustomizer = new SecureASTCustomizer();
            ArrayList<Integer> disallowedTokens = new ArrayList<>();
            disallowedTokens.add(Types.KEYWORD_WHILE);
            disallowedTokens.add(Types.KEYWORD_GOTO);
            disallowedTokens.add(Types.KEYWORD_FOR);
            secureASTCustomizer.setTokensBlacklist(disallowedTokens);
            config.addCompilationCustomizers(secureASTCustomizer);
            config.setScriptBaseClass("moe.aira.shell.AiraScript");
            GroovyShell groovyShell = new GroovyShell(this.getClass().getClassLoader(), binding, config);
            try {
                Object evaluate;
                evaluate = groovyShell.evaluate(substring);
                if (evaluate != null) {
                    AiraSendMessageUtil.sendMessage(bot, event, Objects.toString(evaluate));
                }
            } catch (Exception e) {
                String message = e.getMessage();
                AiraSendMessageUtil.sendMessage(bot, event, Objects.toString(message));

            }
        };
    }
}
