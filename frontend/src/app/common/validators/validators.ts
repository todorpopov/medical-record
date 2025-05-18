import {AbstractControl, ValidationErrors, ValidatorFn} from '@angular/forms';

export function allowedValuesValidator(allowedValues: string[]): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    if (value === null || value === undefined || value === '') {
      return null;
    }
    return allowedValues.includes(value) ? null : { allowedValues: { value, allowed: allowedValues } };
  }
}

export function isoDateValidator(): ValidatorFn {
  const isoDateRegex = /^\d{4}-\d{2}-\d{2}$/;

  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (!value) return null;

    if (!isoDateRegex.test(value)) {
      return { isoDate: { value } };
    }

    const date = new Date(value);
    const isValidDate = !isNaN(date.getTime()) && value === date.toISOString().split('T')[0];

    return isValidDate ? null : { isoDate: { value } };
  };
}

export function timeValidator(): ValidatorFn {
  const timeRegex = /^([01]\d|2[0-3]):([0-5]\d)$/;

  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (!value) return null;

    return timeRegex.test(value) ? null : { time: { value } };
  };
}
