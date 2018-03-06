/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation;

import implementation.AllMyLatches.*;
import utilitytypes.EnumOpcode;
import baseclasses.InstructionBase;
import baseclasses.PipelineRegister;
import baseclasses.PipelineStageBase;
import voidtypes.VoidLatch;
import baseclasses.CpuCore;
import utilitytypes.Operand;

/**
 * The AllMyStages class merely collects together all of the pipeline stage 
 * classes into one place.  You are free to split them out into top-level
 * classes.
 * 
 * Each inner class here implements the logic for a pipeline stage.
 * 
 * It is recommended that the compute methods be idempotent.  This means
 * that if compute is called multiple times in a clock cycle, it should
 * compute the same output for the same input.
 * 
 * How might we make updating the program counter idempotent?
 * 
 * @author
 */
public class AllMyStages {
    /*** Fetch Stage ***/
    static class Fetch extends PipelineStageBase<VoidLatch,FetchToDecode> {
        public Fetch(CpuCore core, PipelineRegister input, PipelineRegister output) {
            super(core, input, output);
        }
        
        @Override
        public String getStatus() {
            // Generate a string that helps you debug.
        	System.out.println("Unable to get the status");
            return null;
        }

        @Override
        public void compute(VoidLatch input, FetchToDecode output) {
        	System.out.println("Inside Fetch Stage");
            GlobalData globals = (GlobalData)core.getGlobalResources();
            int pc = globals.program_counter;
            // Fetch the instruction
            InstructionBase ins = globals.program.getInstructionAt(pc);
            if (ins.isNull()) {
            	System.out.println("No instruction at this point of time\n\n");
            	return;
            }
            System.out.println("Instruction is:"+ins+"\n\n");
            //System.out.println("Program counter:"+pc);
            // Do something idempotent to compute the next program counter.
            
            // Don't forget branches, which MUST be resolved in the Decode
            // stage.  You will make use of global resources to commmunicate
            // between stages.
            
            // Your code goes here...
            globals.program_counter++;
            
            output.setInstruction(ins);
        }
        
        @Override
        public boolean stageWaitingOnResource() {
            // Hint:  You will need to implement this for when branches
            // are being resolved.
            return false;
        }
        
        
        /**
         * This function is to advance state to the next clock cycle and
         * can be applied to any data that must be updated but which is
         * not stored in a pipeline register.
         */
        @Override
        public void advanceClock() {
            // Hint:  You will need to implement this help with waiting
            // for branch resolution and updating the program counter.
            // Don't forget to check for stall conditions, such as when
            // nextStageCanAcceptWork() returns false.
        	
        }
    }

    
    /*** Decode Stage ***/
    static class Decode extends PipelineStageBase<FetchToDecode,DecodeToExecute> {
        public Decode(CpuCore core, PipelineRegister input, PipelineRegister output) {
            super(core, input, output);
        }
        
        @Override
        public boolean stageWaitingOnResource() {
            // Hint:  You will need to implement this to deal with 
            // dependencies.
            return false;
        }
        

        @Override
        public void compute(FetchToDecode input, DecodeToExecute output) {
        	System.out.println("Inside the Decode stage");
            InstructionBase ins = input.getInstruction();
            
            // These null instruction checks are mostly just to speed up
            // the simulation.  The Void types were created so that null
            // checks can be almost completely avoided.
            if (ins.isNull()) {
            	System.out.println("No instruction at this point of time\n\n");
            	return;
            }
            System.out.println("Instruction is:"+ins+"\n\n");
            
            GlobalData globals = (GlobalData)core.getGlobalResources();
            int[] regfile = globals.register_file;
            
            // Do what the decode stage does:
            // - Look up source operands
            // - Decode instruction
            // - Resolve branches 
            //Operand Destination = ins.getOper0();
            //System.out.println("Destination is:"+Destination.getRegisterNumber());
            output.setInstruction(ins);
            // Set other data that's passed to the next stage.
        }
    }
    

    /*** Execute Stage ***/
    static class Execute extends PipelineStageBase<DecodeToExecute,ExecuteToMemory> {
        public Execute(CpuCore core, PipelineRegister input, PipelineRegister output) {
            super(core, input, output);
        }

        @Override
        public void compute(DecodeToExecute input, ExecuteToMemory output) {
        	System.out.println("Inside the Execute stage");
            InstructionBase ins = input.getInstruction();
            if (ins.isNull()) {
            	System.out.println("No instruction at this point of time\n\n");
            	return;
            }
            System.out.println("Instruction is:"+ins+"\n\n");

            int source1 = ins.getSrc1().getValue();
            int source2 = ins.getSrc2().getValue();
            int oper0 =   ins.getOper0().getValue();
            int result = MyALU.execute(ins.getOpcode(), source1, source2, oper0);
                        
            // Fill output with what passes to Memory stage...
            output.setInstruction(ins);
            output.result_of_execute=result;
            // Set other data that's passed to the next stage.
        }
    }
    

    /*** Memory Stage ***/
    static class Memory extends PipelineStageBase<ExecuteToMemory,MemoryToWriteback> {
        public Memory(CpuCore core, PipelineRegister input, PipelineRegister output) {
            super(core, input, output);
        }

        @Override
        public void compute(ExecuteToMemory input, MemoryToWriteback output) {
        	System.out.println("Inside the Memory Stage");
            InstructionBase ins = input.getInstruction();
            if (ins.isNull()) {
            	System.out.println("No instruction at this point of time\n\n");
            	return;
            }
            System.out.println("Instruction is:"+ins+"\n\n");
            // Access memory...
            switch(input.getInstruction().getOpcode())
            {
            	case LOAD:
            		break;
            	case STORE:
            		break;
            	default:
            		break;
            }
            output.setInstruction(ins);
            output.result_of_memory = input.result_of_execute;
            // Set other data that's passed to the next stage.
        }
    }
    

    /*** Writeback Stage ***/
    static class Writeback extends PipelineStageBase<MemoryToWriteback,VoidLatch> {
        public Writeback(CpuCore core, PipelineRegister input, PipelineRegister output) {
            super(core, input, output);
        }

        @Override
        public void compute(MemoryToWriteback input, VoidLatch output) {
        	System.out.println("Inside the Writeback stage");
            InstructionBase ins = input.getInstruction();
            if (ins.isNull()) { 
            	System.out.println("No instruction at this point of time\n\n");
            	return;
            }
            System.out.println("Instruction is:"+ins+"\n\n");
            //GlobalData globals = new GlobalData();
            // Write back result to register file
            
            if(EnumOpcode.needsWriteback(ins.getOpcode())) {
            	//System.out.println("Should do something in the writeback stage");
            	ins.getOper0().setValue(input.result_of_memory);
            	//System.out.println("Register to which the value to be written is:"+ins.getOper0().getRegisterNumber());
            	//System.out.println("Value written to register is "+input.result_of_memory+"\n");
            }
            
            if (input.getInstruction().getOpcode() == EnumOpcode.HALT) {
            	System.out.println("HALT encountered");
                GlobalData.is_halt = true;
            }
        }
    }
}
