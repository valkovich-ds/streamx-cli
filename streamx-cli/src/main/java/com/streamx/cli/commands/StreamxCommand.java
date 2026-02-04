package com.streamx.cli.commands;

import com.streamx.cli.commands.local.LocalCommand;
import com.streamx.cli.framework.AbstractCommandGroup;
import com.streamx.cli.util.VersionProvider;
import picocli.CommandLine;

@CommandLine.Command(
    name = "streamx",
    mixinStandardHelpOptions = true,
    description = "StreamX CLI. More info at https://streamx.dev",
    subcommands = {
        LocalCommand.class
    },
    versionProvider = VersionProvider.class
)
public class StreamxCommand extends AbstractCommandGroup {
}
