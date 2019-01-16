package ru.sportequipment.command;

import ru.sportequipment.command.admin.*;
import ru.sportequipment.command.guest.LogInCommand;
import ru.sportequipment.command.guest.RegisterCommand;
import ru.sportequipment.command.user.*;
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
    REGISTER {
        {
            this.command = new RegisterCommand();
            this.role = EnumSet.of(RoleEnum.GUEST);
        }
    },
    LOGOUT {
        {
            this.command = new LogOutCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN, RoleEnum.USER);
        }
    },


    GET_CONTACT {
        {
            this.command = new GetContactCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN, RoleEnum.USER);
        }
    },
    GET_CONTACTS {
        {
            this.command = new GetContactsCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN, RoleEnum.USER);
        }
    },
    UPDATE_CONTACT {
        {
            this.command = new UpdateContactCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN, RoleEnum.USER);
        }
    },
    CREATE_CONTACT {
        {
            this.command = new CreateContactCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN);
        }
    },
    DELETE_CONTACT {
        {
            this.command = new DeleteContactCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN);
        }
    },

    GET_SKATES {
        {
            this.command = new GetSkatesCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN, RoleEnum.USER);
        }
    },
    REFRESH_SKATES {
        {
            this.command = new RefreshSkatesCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN, RoleEnum.USER);
        }
    },
    DELETE_SKATES {
        {
            this.command = new DeleteSkatesCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN);
        }
    },
    CREATE_SKATES {
        {
            this.command = new CreateSkatesCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN);
        }
    },
    UPDATE_SKATES {
        {
            this.command = new UpdateSkatesCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN);
        }
    },


    GET_STICKS {
        {
            this.command = new GetSticksCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN, RoleEnum.USER);
        }
    },
    REFRESH_STICKS {
        {
            this.command = new RefreshSticksCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN, RoleEnum.USER);
        }
    },
    UPDATE_STICK {
        {
            this.command = new UpdateStickCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN);
        }
    },
    CREATE_STICK {
        {
            this.command = new CreateStickCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN);
        }
    },
    DELETE_STICK {
        {
            this.command = new DeleteStickCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN);
        }
    },
    GENERATE_REPORT {
        {
            this.command = new GenerateReport();
            this.role = EnumSet.of(RoleEnum.ADMIN, RoleEnum.USER);
        }
    },


    BOOK_STICK {
        {
            this.command = new BookStickCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN, RoleEnum.USER);
        }
    },
    BOOK_SKATES {
        {
            this.command = new BookSkatesCommand();
            this.role = EnumSet.of(RoleEnum.ADMIN, RoleEnum.USER);
        }
    },


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
