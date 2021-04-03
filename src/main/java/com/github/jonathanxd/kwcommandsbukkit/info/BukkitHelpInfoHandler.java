/*
 *      KWCommandsBukkit - Register KWCommands in Bukkit. <https://github.com/JonathanxD/KWCommandsBukkit>
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2021 JonathanxD <jonathan.scripter@programmer.net>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
 */
package com.github.jonathanxd.kwcommandsbukkit.info;

import com.github.jonathanxd.kwcommands.fail.ParseFail;
import com.github.jonathanxd.kwcommands.help.CommonHelpInfoHandler;
import com.github.jonathanxd.kwcommands.help.HelpInfoHandler;
import com.github.jonathanxd.kwcommands.printer.Printer;
import com.github.jonathanxd.kwcommands.processor.CommandResult;
import com.github.jonathanxd.kwcommands.processor.UnsatisfiedRequirementsResult;
import com.github.jonathanxd.kwcommands.requirement.RequirementTester;
import com.github.jonathanxd.kwcommands.requirement.UnsatisfiedRequirement;
import com.github.jonathanxd.kwcommandsbukkit.Texts;
import com.github.jonathanxd.kwcommandsbukkit.req.PermissionRequirementTest;

import java.util.ArrayList;
import java.util.List;

public final class BukkitHelpInfoHandler implements HelpInfoHandler {
    private final HelpInfoHandler wrapped = new CommonHelpInfoHandler();

    @Override
    public void handleFail(ParseFail parseFail, Printer printer) {
        this.wrapped.handleFail(parseFail, printer);
    }

    @Override
    public void handleResult(CommandResult commandResult, Printer printer) {
        if (!this.handlePermResult(commandResult, printer))
            this.wrapped.handleResult(commandResult, printer);
    }

    @Override
    public void handleResults(List<? extends CommandResult> list, Printer printer) {
        List<CommandResult> results = new ArrayList<>();

        for (CommandResult commandResult : list) {
            if (commandResult instanceof UnsatisfiedRequirementsResult) {
                List<UnsatisfiedRequirement<?>> reqs =
                        new ArrayList<>(((UnsatisfiedRequirementsResult) commandResult).getUnsatisfiedRequirements());

                reqs.removeIf(unsatisfiedRequirement -> this.handlePermResult(unsatisfiedRequirement, printer));

                if (!reqs.isEmpty()) {
                    UnsatisfiedRequirementsResult r = new UnsatisfiedRequirementsResult(reqs,
                            commandResult.getRootContainer(),
                            commandResult.getContainer());
                    results.add(r);
                }
            } else {
                results.add(commandResult);
            }

        }

        if (!results.isEmpty())
            wrapped.handleResults(results, printer);
    }

    private boolean handlePermResult(CommandResult commandResult, Printer printer) {
        if (commandResult instanceof UnsatisfiedRequirementsResult) {
            for (UnsatisfiedRequirement<?> unsatisfiedRequirement :
                    ((UnsatisfiedRequirementsResult) commandResult).getUnsatisfiedRequirements()) {

                if (this.isPerm(unsatisfiedRequirement)) {
                    printer.printPlain(Texts.I.getPermissionText());
                    printer.flush();
                    return true;
                }
            }
        }

        return false;
    }

    private boolean handlePermResult(UnsatisfiedRequirement<?> unsatisfiedRequirement, Printer printer) {

        if (this.isPerm(unsatisfiedRequirement)) {
            printer.printPlain(Texts.I.getPermissionText());
            printer.flush();
            return true;
        }

        return false;
    }

    private boolean isPerm(UnsatisfiedRequirement<?> unsatisfiedRequirement) {
        RequirementTester<?, ?> tester = unsatisfiedRequirement.getRequirement().getTester();

        return tester instanceof PermissionRequirementTest;

    }
}
