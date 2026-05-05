export interface Scheme {
  id: string;
  name: string;
  description: string;
  documents: string[];
  eligibility: {
    maxIncome?: number;
    bplRequired?: boolean;
    occupations?: string[];
  };
}

export interface Hospital {
  id: string;
  name: string;
  district: string;
  schemes: string[];
  type: 'Public' | 'Private';
}

export const SCHEMES: Scheme[] = [
  {
    id: 's1',
    name: 'Ayushman Bharat (PM-JAY)',
    description: 'Provides health cover of Rs. 5 lakhs per family per year for secondary and tertiary care hospitalization.',
    documents: ['Aadhar Card', 'Rashan Card', 'PMJAY Letter / Golden Card'],
    eligibility: {
      bplRequired: true,
    }
  },
  {
    id: 's2',
    name: 'Rashtriya Swasthya Bima Yojana (RSBY)',
    description: 'Health insurance scheme for Below Poverty Line (BPL) families in the unorganized sector.',
    documents: ['BPL Card', 'Aadhar Card', 'Passport Size Photo'],
    eligibility: {
      bplRequired: true,
      occupations: ['Unorganized Worker', 'Farmer', 'Daily Wage']
    }
  },
  {
    id: 's3',
    name: 'Chief Minister\'s Comprehensive Health Insurance',
    description: 'State-specific health protection scheme for low-income families.',
    documents: ['Income Certificate', 'Aadhar Card', 'Family ID (Ration Card)'],
    eligibility: {
      maxIncome: 120000 
    }
  },
  {
    id: 's4',
    name: 'Employee State Insurance (ESI)',
    description: 'Social security and health insurance scheme for Indian workers.',
    documents: ['ESI e-Pehchan Card', 'Aadhar Card', 'Employment Proof'],
    eligibility: {
      maxIncome: 252000, // 21k per month
      occupations: ['Salaried']
    }
  }
];

export const HOSPITALS: Hospital[] = [
  { id: 'h1', name: 'District General Hospital', district: 'Mumbai', schemes: ['s1', 's2', 's3'], type: 'Public' },
  { id: 'h2', name: 'Sanjeevani Clinic', district: 'Mumbai', schemes: ['s1', 's4'], type: 'Private' },
  { id: 'h3', name: 'City Care Hospital', district: 'Delhi', schemes: ['s1', 's2', 's3', 's4'], type: 'Private' },
  { id: 'h4', name: 'Government Medical College', district: 'Delhi', schemes: ['s1', 's2', 's3'], type: 'Public' },
  { id: 'h5', name: 'Rural Health Center', district: 'Pune', schemes: ['s1', 's2', 's3'], type: 'Public' },
  { id: 'h6', name: 'Apex Medicore', district: 'Pune', schemes: ['s1', 's4'], type: 'Private' },
  // Add some generic districts to match easily
  { id: 'h7', name: 'Bangalore General', district: 'Bangalore', schemes: ['s1', 's2', 's3'], type: 'Public' },
  { id: 'h8', name: 'Chennai Health Trust', district: 'Chennai', schemes: ['s1', 's3', 's4'], type: 'Public' },
];

export const OCCUPATIONS = [
  'Farmer',
  'Daily Wage',
  'Unorganized Worker',
  'Salaried',
  'Self-Employed',
  'Unemployed'
];

export const DISTRICTS = ['Mumbai', 'Delhi', 'Pune', 'Bangalore', 'Chennai', 'Kolkata', 'Hyderabad'];
