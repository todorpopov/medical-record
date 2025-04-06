import { NgFor, NgIf } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-dropdown',
  imports: [
    NgIf,
    NgFor,
    FormsModule
  ],
  templateUrl: './dropdown.component.html',
  styleUrl: './dropdown.component.css'
})
export class DropdownComponent<T> {
  @Input() items: T[] = [];
  @Input() label = '';
  @Input() placeholder = 'Select an option';
  @Input() valueField = 'id';
  @Input() displayField = 'name';
  @Input() disabled = false;
  @Input() errorMessage = '';
  
  @Input() selectedValue: any = '';
  @Output() selectedValueChange = new EventEmitter<any>();
  @Output() selectionChanged = new EventEmitter<T>();

  getItemValue(item: T): any {
    if (typeof item === 'object' && item !== null) {
      return (item as any)[this.valueField];
    }
    return item;
  }

  getItemLabel(item: T): string {
    if (typeof item === 'object' && item !== null) {
      return (item as any)[this.displayField];
    }
    return String(item);
  }

  onSelectionChange(value: any) {
    this.selectedValueChange.emit(value);
    const selectedItem = this.items.find(item => this.getItemValue(item) === value);
    if (selectedItem) {
      this.selectionChanged.emit(selectedItem);
    }
  }
}
