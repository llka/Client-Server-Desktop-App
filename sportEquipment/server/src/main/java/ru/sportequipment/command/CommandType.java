package ru.sportequipment.command;

import ru.sportequipment.command.guest.LogInCommand;
import ru.sportequipment.entity.enums.RoleEnum;

import java.util.EnumSet;
import java.util.Set;

public enum CommandType {
    LOGIN {
        {
            this.command = new LogInCommand();
            this.role = EnumSet.of(RoleEnum.GUEST);
        }
    },
    //    REGISTER {
//        {
//            this.command = new RegisterCommand();
//            this.role = EnumSet.of(RoleEnum.GUEST);
//        }
//    },
//
//    LOGOUT {
//        {
//            this.command = new LogOutCommand();
//            this.role = EnumSet.of(RoleEnum.ADMIN, RoleEnum.USER);
//        }
//    },
    EMPTY {
        {
            this.command = new EmptyCommand();
            this.role = EnumSet.of(RoleEnum.GUEST, RoleEnum.USER, RoleEnum.ADMIN);
        }
    };

    public ActionCommand command;
    public Set<RoleEnum> role;

    public ActionCommand getCommand() {
        return command;
    }
}
