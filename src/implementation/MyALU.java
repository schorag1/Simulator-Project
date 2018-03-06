/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import utilitytypes.EnumOpcode;

/**
 * The code that implements the ALU has been separates out into a static
 * method in its own class.  However, this is just a design choice, and you
 * are not required to do this.
 * 
 * @author 
 */
public class MyALU {
    static int execute(EnumOpcode opcode, int input1, int input2, int oper0) {
        int result = 0;
        
        // Implement code here that performs appropriate computations for
        // any instruction that requires an ALU operation.  See
        // EnumOpcode.
        
        switch(opcode) {
        	case ADD:	
        		System.out.println("Executing ADD opcode:");
        		result = input1 + input2;
        		System.out.println("Temporary result after addition is "+result);
        		break;
        	
        	case SUB:
        		System.out.println("Executing SUB opcode:");
        		result = input1 - input2;
        		System.out.println("Temporary result after subtraction is "+result);
        		break;
        		
        	case AND:
        		System.out.println("Executing AND opcode:");
        		result = input1 & input2;
        		System.out.println("Temporary result after Bitwise AND is "+result);
        		break;
        		
        	case OR:
        		System.out.println("Executing OR opcode:");
        		result = input1 | input2;
        		System.out.println("Temporary result after Bitwise OR is "+result);
        		break;
        		
        	case XOR:
        		System.out.println("Executing XOR opcode:");
        		result = input1 ^ input2;
        		System.out.println("Temporary result after Bitwise XOR is "+result);
        		break;
        		
        	case SHL:
        		System.out.println("Executing SHL opcode:");
        		result = input1 << input2;
        		System.out.println("Temporary result after Logical Shift Left is "+result);
        		break;
        		
        	case LSR:
        		System.out.println("Executing LSR opcode:");
        		result = input1 >> input2;
        		System.out.println("Temporary result after Logical Shift Right is "+result);
        		break;
        		
        	case ASR:
        		System.out.println("Executing ASR opcode:");
        		result = input1 >>> input2;
        		System.out.println("Temporary result after Arithmetic Shift Right is "+result);
        		break;
        		
        	case MOVC:
        		System.out.println("Executing MOVC opcode:");
        		result = input1;
        		System.out.println("Moved to result:"+result);
        		break;
        		
        	case OUT:
        		System.out.println("Executing OUT opcode:");
        		System.out.println("Contents of the register:"+oper0);
        		break;
        		
        		
			case BRA:
				break;
			case CALL:
				break;
			case CMP:
				break;
			case DIVS:
				break;
			case DIVU:
				break;
			case HALT:
				break;
			case INVALID:
				break;
			case JMP:
				break;
			case LOAD:
				break;
			case MULS:
				break;
			case MULU:
				break;
			case NOP:
				break;
			case NULL:
				break;
			case ROL:
				break;
			case ROR:
				break;
			case STORE:
				break;
			default:
				break;
        }
        
        return result;
    }    
}
