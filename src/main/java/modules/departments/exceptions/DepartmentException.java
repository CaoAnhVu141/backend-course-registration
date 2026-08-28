package modules.departments.exceptions;

import modules.departments.enums.EnumDepartmentError;

public class DepartmentException extends RuntimeException {

    public DepartmentException(EnumDepartmentError error) {
        super(error.getMessage());
    }
}