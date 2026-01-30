package com.streamx.cli.commands;

import com.streamx.cli.commands.local.LocalCommand;
import com.streamx.cli.framework.AbstractCommandGroup;
import picocli.CommandLine;

@CommandLine.Command(
    name = "streamx",
    mixinStandardHelpOptions = true,
    description = "StreamX CLI. More info at https://streamx.dev",
    subcommands = {
        LocalCommand.class
    }
)
public class StreamxCommand extends AbstractCommandGroup {
}
