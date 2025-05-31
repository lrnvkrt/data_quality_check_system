import React from 'react'
import { List } from '@mui/material'
import ExpectationListItem from './ExpectationListItem'

export default function ExpectationList({ expectations, onChanged }) {
    return (
        <List disablePadding dense>
            {expectations.map((exp) => (
                <ExpectationListItem
                    key={exp.id}
                    expectation={exp}
                    onChanged={onChanged}
                />
            ))}
        </List>
    )
}