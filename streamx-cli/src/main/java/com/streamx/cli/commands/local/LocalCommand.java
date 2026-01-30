package com.streamx.cli.commands.local;

import com.streamx.cli.framework.AbstractCommandGroup;
import com.streamx.cli.commands.local.run.RunCommand;
import picocli.CommandLine;

@CommandLine.Command(
    name = "local",
    mixinStandardHelpOptions = true,
    description = "Operate local StreamX instance",
    subcommands = {
        RunCommand.class,
    }
)
public class LocalCommand extends AbstractCommandGroup {
}
