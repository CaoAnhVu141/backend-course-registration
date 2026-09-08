package modules.defense_committees.exception;

import modules.defense_committees.enums.EnumDefenseCommitteeError;

public class DefenseCommitteeException extends RuntimeException {

    public DefenseCommitteeException(EnumDefenseCommitteeError error) {
        super(error.getMessage());
    }
}
